#import tensorflow as tf
import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()

import numpy as np
import math
from pprint import pprint
from scipy.ndimage.filters import gaussian_filter
import matplotlib.pyplot as plt
from PIL import Image
import copy
from timeit import default_timer as timer


import configs as cfgs
import utils
import psnr


def setup_testing(prob, decoded_,):
    cost_ = tf.reduce_mean(tf.square(prob.targets_ - decoded_))
    ssim_ = tf.image.ssim(prob.targets_, decoded_, max_val=255.0, filter_size=11,filter_sigma=1.5, k1=0.01, k2=0.03)

    return cost_,ssim_

def do_testing(filename, sess, prob, cost, decoded_, features_,features2_,ssim):

    sess.run(tf.global_variables_initializer())
    load_trainable_vars(sess, filename)

    sum_ssim = 0
    sum_cost = 0
    sum_psnr = 0
    start=timer()
    for j in range(prob.num_test_data):
        noisy_imgs, imgs,noisy_imgs2 = prob.next_test_sample(j)
            
        input_img   = [noisy_imgs]
        target_img  = [imgs]
        input_img2  = [noisy_imgs2]

        batch_cost  = sess.run(cost, feed_dict={prob.inputs_:input_img, prob.targets_:target_img, prob.inputs2_:input_img2})
        decoded     = sess.run(decoded_, feed_dict={prob.inputs_: input_img,prob.inputs2_:input_img2})
        ssim2        = sess.run(ssim, feed_dict={prob.inputs_:input_img, prob.targets_:target_img, prob.inputs2_:input_img2})
        #print(decoded.dtype)
        #print(imgs.dtype)

        PSNR = psnr.psnr(imgs[:, :, 0], noisy_imgs[:, :, 0])
        print("input - target: %f",PSNR)
           
        PSNR = psnr.psnr(imgs[:, :, 0], decoded[0, :, :, 0])
        print("decoded - target: %f",PSNR)

        #print(ssim2)

        sum_cost = sum_cost + batch_cost
        sum_psnr = sum_psnr + PSNR
        sum_ssim = sum_ssim + ssim2
        
        if j==17:
            blurred = gaussian_filter(noisy_imgs[:, :, 0], sigma=1.5)
            plot_testing_results(blurred,  imgs[:, :, 0], decoded[0, :, :, 0])
            
    end=timer()
    print("Elapsed time is : ", (end-start))
    print("Average loss: %f, Average PSNR : %f, Average SSIM : %f"% (float(sum_cost/(prob.num_test_data) ), float(sum_psnr/(prob.num_test_data ) ), (sum_ssim/(prob.num_test_data) ) ))

def plot_testing_results(noisy_imgs, imgs, decoded):
    f = plt.figure()
    f.add_subplot(1,3, 1)
    plt.imshow(noisy_imgs, cmap='gray')
    plt.title('input')
    f.add_subplot(1,3, 2)
    plt.imshow(imgs, cmap='gray' )
    plt.title('ground truth')
    f.add_subplot(1,3, 3)
    plt.imshow(decoded, cmap='gray')
    plt.title('estimated ')
    plt.show(block=True)


def plot_features(features):
    f = plt.figure()
    plt.plot(features)
    plt.title('features')
    plt.show(block=True)



def load_trainable_vars(sess,filename):
    """load a .npz archive and assign the value of each loaded
    ndarray to the trainable variable whose name matches the
    archive key.  Any elements in the archive that do not have
    a corresponding trainable variable will be returned in a dict.
    """
    print("Loading learned variables from %s"%filename)
    other={}
    for v in tf.trainable_variables():
        print(v.name)

    try:
        tv=dict([ (str(v.name),v) for v in tf.trainable_variables() ])
        for k,d in np.load(filename).items():
            if k in tv:
                print('restoring ' + k)
                sess.run(tf.assign( tv[k], d) )
            else:
                other[k] = d
                print("Cannot Load")
    except IOError:
        pass
    return other
