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
	ReentrantLock reeLock = null;
    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
	Condition havePermits = null;
    Condition notTooMuchPermits = null;
    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here. Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
    int mPermits;
    int MAX_PERMITS = 1;
    
    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
    	    	
    	reeLock  = new ReentrantLock(fair);	
    	havePermits = reeLock.newCondition();    
    	notTooMuchPermits = reeLock.newCondition();
    	
    	reeLock.lock();
    	
    	mPermits = permits;    	
    	
    	reeLock.unlock();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
    	reeLock.lock();
    	
    	while(mPermits<=0)
    		havePermits.await();
    	
    	mPermits--;   	 
    	notTooMuchPermits.signal();
    	reeLock.unlock();
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
    	reeLock.lock();
    	
    	while(mPermits<=0)
    		havePermits.awaitUninterruptibly();
    	
    	mPermits--;
    	notTooMuchPermits.signal();
    	
    	reeLock.unlock();
    }

    /**
     * Return one permit to the semaphore.
     */
    void release() {
        // TODO - you fill in here.
    	reeLock.lock();
    	
    	while(mPermits>=MAX_PERMITS)
        	notTooMuchPermits.awaitUninterruptibly();
        
        mPermits++;   
        havePermits.signal();
        
        reeLock.unlock();
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here to return the correct result
    	return mPermits;
    }
}
