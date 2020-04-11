package com.example.demo;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadExport {
    public static void main(String[] args) {
        final AlternateDemo ad = new AlternateDemo();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    ad.loopA();
                }
            }
        }, "a").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    ad.loopB();
                }
            }
        }, "b").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    ad.loopC();
                }
            }
        }, "c").start();
    }

}

class AlternateDemo {

    private int number = 1;

    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    public void loopA() {
        lock.lock();
        try {
            if (number != 1) {
                condition1.await();
            }
            System.out.print(Thread.currentThread().getName());
            number = 2;
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopB() {
        lock.lock();
        try {
            if (number != 2) {
                condition2.await();
            }
            System.out.print(Thread.currentThread().getName());
            number = 3;
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void loopC() {
        lock.lock();
        try {
            if (number != 3) {
                condition3.await();
            }
            System.out.print(Thread.currentThread().getName());
            number = 1;
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}