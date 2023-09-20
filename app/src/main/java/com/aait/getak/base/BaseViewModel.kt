package com.aait.getak.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aait.getak.network.remote_db.Resource
import com.aait.getak.utils.runOnMain
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


open class BaseViewModel : ViewModel() {

     var networkJob = Job()
     val coroutineScope = CoroutineScope(networkJob + Dispatchers.Main )

    private val compositeDisposable = CompositeDisposable()
    val isLoading = MutableLiveData<Boolean>()
    open var states: MutableLiveData<Resource<Any>>? = MutableLiveData()
    protected fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    private fun dispose() = compositeDisposable.dispose()

    override fun onCleared() {
        networkJob.cancel()
        dispose()
        super.onCleared()
    }

    protected fun <T> Single<T>.subscribeWithLoading(onSuccess: (T) -> Unit, onError: (throwable:Throwable) -> Unit = { throw  it}) : Disposable {
        return runOnMain{
            states!!.postValue(Resource.loading())
        }
            .doOnSubscribe { isLoading.value = true }
            .doOnSuccess { onSuccess(it) }
            .doOnError{ onError(it) }
            .doFinally{ isLoading.value = false}
            .subscribe()
    }
    protected fun <T> Observable<T>.subscribeWithLoading(onSuccess: (T) -> Unit, onError: (throwable:Throwable) -> Unit = { throw  it},onComplete:()->Unit
                                                         ) : Disposable {
        return runOnMain{
            states!!.postValue(Resource.loading())
        }
            .doOnSubscribe {
                states!!.postValue(Resource.loading())
            }
            .subscribe({
                onSuccess(it)
            },{
                    onError(it)
            }
            )
    }
}


