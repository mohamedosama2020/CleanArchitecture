package com.test.cleanArchRoomTest.application.peresentation.dashboard

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.cleanArchRoomTest.application.ext.addTo
import com.test.cleanArchRoomTest.application.rx.StickyAction
import com.test.cleanArchRoomTest.data.response.ResponseCharacter
import com.test.cleanArchRoomTest.domain.usecase.GetCharactersUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val getCharactersUseCase: GetCharactersUseCase) :
    ViewModel() {

    val disposables = CompositeDisposable()
    val progressVisible = MutableLiveData<Boolean>()
    val charsList = MutableLiveData<ResponseCharacter>()
    val showErrorGettingChars = StickyAction<Boolean>()

    fun bound() {
        getCharactersUseCase.getCharacters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleResult(it) }
            .addTo(disposables)

    }

    fun unbound() {
        disposables.clear()
    }

    override fun onCleared() {
        super.onCleared()

    }

    private fun handleResult(result: GetCharactersUseCase.Result) {
        when (result) {
            is GetCharactersUseCase.Result.Loading -> progressVisible.value = true
            is GetCharactersUseCase.Result.Success -> charsList.value = result.responseCharacter
            is GetCharactersUseCase.Result.Failure -> showErrorGettingChars.trigger(true)
        }
    }
}