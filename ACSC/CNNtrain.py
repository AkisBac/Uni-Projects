#import tensorflow as tf
import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()

import numpy as np
import matplotlib.pyplot as plt
import random
import math
from timeit import default_timer as timer

import configs as cfgs



def setup_training(prob, decoded_, lista_weights,lista_weights2):
    print("set up training ...")
    cost_ = tf.reduce_mean(tf.square(prob.targets_ - decoded_))
    opt1_ = tf.compat.v1.train.AdamOptimizer(cfgs.learning_rate).minimize(cost_)
    if cfgs.VARIABLE_LEARNING_RATE: 
        opt2_ = tf.compat.v1.train.AdamOptimizer(cfgs.learning_rate*0.1).minimize(cost_)  
    else:
        opt2_ = opt1_
    
    return opt1_, opt2_, cost_ 


def do_training(sess, prob, opt1_ ,opt2_, cost_, filename):
    start=timer()
    print("Start training: num epochs = "+ str(cfgs.epochs))

    sess.run(tf.compat.v1.global_variables_initializer())
    load_trainable_vars(sess, filename)

    num_batches = int(prob.num_examples_train/cfgs.batch_size)
    print("num_batches = "+ str(num_batches))
    
    train_cost = []
    file1 = open("MyFile.txt","a")
    for e in range(cfgs.epochs):
        if e>5000:
            opt_ = opt2_
        else:
            opt_ = opt1_

        epoch_train_cost = 0
        prob.shuffle_data()
        for ii in range(num_batches):
            imgsTarget, imgsInput , imgsInput2 = prob.next_batch(cfgs.batch_size)

            # Noisy images as inputs, original images as targets
            batch_cost, _ = sess.run([cost_, opt_ ], feed_dict={prob.inputs_: imgsInput, prob.targets_: imgsTarget,prob.inputs2_: imgsInput2 })
            train_cost.append(batch_cost)
            epoch_train_cost = epoch_train_cost + batch_cost
        
        #prob.train_batch_indx=0
        print("Epoch: {}/{}: ".format(e+1, cfgs.epochs), "Training loss: {:.7f}".format(epoch_train_cost/num_batches))
        file1.write("Epoch: {} , train_loss : {} \n".format(e+1,(epoch_train_cost/num_batches)))
        save_trainable_vars(sess,'SR.npz')
    end=timer()
    print("Elapsed time is : %f", (end-start)/60)
    file1.close()
    
    plt.plot(train_cost)
    plt.title('training loss')
    plt.savefig('train_loss.png')
    plt.show()




def save_trainable_vars(sess,filename,**kwargs):
    """save a .npz archive in `filename`  with
    the current value of each variable in tf.trainable_variables()
    plus any keyword numpy arrays.
    """
    print("Saving trainable variables to %s"%filename)
    save={}
    for v in tf.compat.v1.trainable_variables():
        save[str(v.name)] = sess.run(v)
        # print(str(v.name))
    save.update(kwargs)
    np.savez(filename,**save)




def load_trainable_vars(sess,filename):
    """load a .npz archive and assign the value of each loaded
    ndarray to the trainable variable whose name matches the
    archive key.  Any elements in the archive that do not have
    a corresponding trainable variable will be returned in a dict.
    """
    print("Loading learned variables from %s"%filename)
    other={}
    try:
        tv=dict([ (str(v.name),v) for v in tf.compat.v1.trainable_variables() ])
        for k,d in np.load(filename).items():
            if k in tv:
                print('restoring ' + k)
                sess.run(tf.compat.v1.assign( tv[k], d) )
            else:
                other[k] = d
                print("Cannot Load " + k)
    except IOError:
        pass
    return other
