import os
""" extract RGB/NIR (VECTORIZED) patches from a NIR/RGB directory with several folders"""
parentpath 	= os.path.abspath('..')
dirname 	= parentpath+'/datasets/'
fname_train = dirname + 'Multispectral_Dataset.h5py'