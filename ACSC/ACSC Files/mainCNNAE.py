#import tensorflow as tf

import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()


import problem
import network
import CNNtrain
import CNNtest
import os
#os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'


TRAIN = 0

print('\n.............................................')
# setup problem
prob = problem.problem()

if TRAIN:
	prob.loadtraindata()
else:
	prob.loadtestdata()

# build network
decoded_, features_, weights, features2_, weights2 = network.build_ACSC(prob.inputs_,prob.inputs2_)

# setup training or testing
if TRAIN:
    opt1_, opt2_, cost_=  CNNtrain.setup_training(prob, decoded_, weights,weights2)
else:
    cost_,ssim_ = CNNtest.setup_testing(prob, decoded_)


print ("Start a new session ...")
sess = tf.compat.v1.Session()

if TRAIN:
    filename = 'SR.npz'
    CNNtrain.do_training(sess, prob, opt1_, opt2_, cost_, filename)
else:
    filename = 'SR.npz' #file of saved model
    CNNtest.do_testing(filename, sess, prob, cost_, decoded_, features_,features2_,ssim_)
