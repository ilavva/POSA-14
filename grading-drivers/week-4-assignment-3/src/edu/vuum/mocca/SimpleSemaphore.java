package edu.vuum.mocca;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore implementation using
 *        Java a ReentrantLock and a ConditionObject (which is accessed via a
 *        Condition). It must implement both "Fair" and "NonFair" semaphore
 *        semantics, just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
	final private ReentrantLock mLock;
	
    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
	private Condition mConditionPermits;
	
    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here. Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
	private volatile int mAvailablePermits = 0;
	
    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
    	mLock = new ReentrantLock(fair);
    	mConditionPermits = mLock.newCondition();
    	mAvailablePermits = permits;
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
    	mLock.lockInterruptibly();
    	try {
    	    while (mAvailablePermits <= 0) {
    	    	mConditionPermits.await(); // await is interruptible
    	    }
    	    --mAvailablePermits;
    	} finally {
    		mLock.unlock();
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
    	try {
    		mLock.lock();
    	    while (mAvailablePermits <= 0) {
    	    	mConditionPermits.awaitUninterruptibly();
    	    }
    	    --mAvailablePermits;
    	} finally {
    		mLock.unlock();
    	}
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here.
    	try {
    		mLock.lock();
    		++mAvailablePermits;
    	    if(mAvailablePermits > 0)
    	    {
    	    	mConditionPermits.signal();
    	    }
    	    
    	   
    	} finally {
    		mLock.unlock();
    	}
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here to return the correct result
    	return  mAvailablePermits;
    }
}
