import os
import numpy as np
import random
import cv2
from PIL import Image

import configs as cfgs


def ycbcr2rgb(im_ycbcr):# RGB output will be in [0,1]
	im_ycbcr = im_ycbcr.astype(np.float32)
	im_ycbcr[:,:,0] = (im_ycbcr[:,:,0]*255.0-16)/(235-16) #to [0, 1]
	im_ycbcr[:,:,1:] = (im_ycbcr[:,:,1:]*255.0-16)/(240-16) #to [0, 1]
	im_ycrcb = im_ycbcr[:,:,(0,2,1)].astype(np.float32)
	im_rgb = cv2.cvtColor(im_ycrcb, cv2.COLOR_YCR_CB2RGB)
	return im_rgb


def rgb2ycbcr(im_rgb):# RGB input should be in [0,1]
	im_rgb = im_rgb.astype(np.float32)
	im_ycrcb = cv2.cvtColor(im_rgb, cv2.COLOR_RGB2YCR_CB)
	im_ycbcr = im_ycrcb[:,:,(0,2,1)].astype(np.float32)
	im_ycbcr[:,:,0] = (im_ycbcr[:,:,0]*(235-16)+16)/255.0 #to [16/255, 235/255]
	im_ycbcr[:,:,1:] = (im_ycbcr[:,:,1:]*(240-16)+16)/255.0 #to [16/255, 240/255]
	return im_ycbcr


def convertYUV2RGB(img):
	y  = img[:, :, 0]
	cb = img[:, :, 1]
	cr = img[:, :, 2]

	y = np.array(y).astype(np.uint8)
	cb = np.array(cb).astype(np.uint8)
	cr = np.array(cr).astype(np.uint8)

	y  = Image.fromarray(y)
	cb = Image.fromarray(cb)
	cr = Image.fromarray(cr)

	yuv_img =Image.merge('YCbCr', (y, cb, cr))  
	rgb_img = yuv_img.convert('RGB')
	img_arr = np.array(rgb_img).astype(np.uint8)

	# print(img_arr.min())
	# print(img_arr.max())

	return img_arr
