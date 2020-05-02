package com.walterscarborough.trafficcop.loadtest

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.security.Permission

@Service
class AsyncService {
    @Async
    fun doWork(runnable: Runnable) {
        runnable.run()
    }
}

class MySecurityManager : SecurityManager() {
    override fun checkExit(status: Int) {
        throw SecurityException()
    }

    override fun checkPermission(perm: Permission) {}
}
