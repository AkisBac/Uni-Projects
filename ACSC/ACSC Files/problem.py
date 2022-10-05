#import tensorflow as tf
import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()

import os
import numpy as np
import random
import h5py
import cv2

from pprint import pprint
import matplotlib.pyplot as plt
from PIL import Image
from io import StringIO
import configs as cfgs
import dataconfigs as datacfgs
import psnr
import utils

NUM_CHANNELS = cfgs.NUM_CHANNELS
IMAGE_SIZE 	 = cfgs.IMAGE_SIZE
IMAGE_SIZE2 	 = cfgs.IMAGE_SIZE2

TRAIN_DATASET = datacfgs.fname_train

class problem():

	def __init__(self):
		self.inputs_  = tf.compat.v1.placeholder(tf.float32, (None, IMAGE_SIZE, IMAGE_SIZE, NUM_CHANNELS), name='inputs')
		self.inputs2_  = tf.compat.v1.placeholder(tf.float32, (None, IMAGE_SIZE, IMAGE_SIZE, NUM_CHANNELS), name='inputs2')
		self.targets_ = tf.compat.v1.placeholder(tf.float32, (None, IMAGE_SIZE, IMAGE_SIZE, NUM_CHANNELS), name='targets')
		self.targets2_ = tf.compat.v1.placeholder(tf.float32, (None, IMAGE_SIZE, IMAGE_SIZE, NUM_CHANNELS), name='targets2')
		self.num_examples_train = 5000
		self.num_examples_test = 160
		self.train_batch_indx	= 0
	
	def loadtraindata(self):
		fname = TRAIN_DATASET
		print('----------------------')
		print('Visit dataset')
		print(fname)
		print('----------------------')
		self.visit_dataset_groups(fname)
		print('----------------------')

		self.XI = self.fetch_data(fname, 'TEST_DATASET_x2', 0, self.num_examples_train)
		self.YI = self.fetch_data(fname, 'TEST_DATASET', 0, self.num_examples_train)
		
		new_data = []
		for a in self.XI:								# Keep 1 channel from 3
			new_data.append(a[:,:,:1])
		#print(np.array(new_data).shape)

		new_data2 = []
		for a in self.YI:								# Keep 1 channel from 3
			new_data2.append(a[:,:,:1])		
 
		self.XI = np.array(new_data)
		self.YI = np.array(new_data2)
		self.ZI = self.YI
		#self.train_batch_indx = 0
		N, m, n, q = self.XI.shape 
		self.num_examples_train = N

	def loadtestdata(self):
		self.loadtraindata()
		self.num_test_data = 30
		


	def fetch_data(self, filename, dataset, m, n):
		"Load data from filename h5py dataset"
		print('Get data from dataset: ' + dataset)
		f = h5py.File(filename, 'r') # open file in "append" mode
		size_tuple = f[dataset].shape
		print('Loaded dataset shape:')
		print(size_tuple)
		print('Returned dataset size:', n-m)
		return f[dataset][m:n]

	def get_all(self, name):
		print(name)

	def visit_dataset_groups(self, fname):
		f = h5py.File(fname, 'r')
		print('Loading dataset: ' + fname)
		print('The following groups and subgroups are found:')
		f.visit(self.get_all)
		
	
	# -------------------------------------------- #
	# LOAD DATA FOR TRAINING/TESTING
	# -------------------------------------------- #
	def next_batch(self, batch_size):
		target_samples  = self.ZItrain[self.train_batch_indx:self.train_batch_indx+batch_size]
		input_samples 	= self.XItrain[self.train_batch_indx:self.train_batch_indx+batch_size]
		#target_samples2  = self.ZItrain[batch_size:batch_size+batch_size]
		input_samples2 	= self.XItrain[self.train_batch_indx:self.train_batch_indx+batch_size]
		self.train_batch_indx = self.train_batch_indx + batch_size
		return target_samples, input_samples  , input_samples2

	def next_test_sample(self, noexample):
		target_sample   = self.XI[noexample]
		input_sample  	= self.ZI[noexample]
		input_sample2   = self.XI[noexample]
		return target_sample, input_sample, input_sample2

	def shuffle_data(self):
		self.train_batch_indx = 0
		T = self.num_examples_train
		index_list = list(range(self.num_examples_train))
		#index_list_target = list(range(self.num_examples_test))
		random.shuffle(index_list)
		self.XItrain = self.shuffled_traininglist(index_list, self.XI)
		self.ZItrain = self.shuffled_traininglist(index_list, self.ZI)


	def shuffled_traininglist(self, index_list, dataset):
		datalist = []
		for index in index_list:
			datalist.append(dataset[index])
		return datalist

	
	

if __name__ == "__main__":
	TRAIN = 0
	prob = problem()
	
	if TRAIN:
		prob.loadtraindata()

		indx = 0
		extarget = prob.XI[indx, :, :, 0]
		exinput	 = prob.ZI[indx, :, :, 0]

		prob.shuffle_data()
		extarget = prob.XItrain[indx][:, :, 0]
		exinput	 = prob.ZItrain[indx][:, :, 0]
		

		batch_size = 10
		target_samples, input_samples  = prob.next_batch(batch_size)
		extarget = target_samples[indx][:, :, 0]
		exinput	 = input_samples[indx][:, :, 0]
		
	else:
		prob.loadtestdata()
		noexample = 10
		extarget, exinput = prob.next_test_sample(noexample)

		extarget = extarget[:, :, 0]
		exinput = exinput[:, :, 0]
		indx = 0
		
		

	if 1:
		f = plt.figure()
		f.add_subplot(2,2,1)
		plt.imshow(extarget, cmap = 'gray')
		plt.title('target image')
		f.add_subplot(2,2,2)
		plt.imshow(exinput, cmap = 'gray')
		plt.title('input image')
		plt.show()


 # # 