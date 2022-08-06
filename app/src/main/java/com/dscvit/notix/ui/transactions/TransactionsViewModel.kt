package com.dscvit.notix.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dscvit.notix.database.NotixRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TransactionsViewModel @Inject constructor(
    application: Application,
    private val repository: NotixRepository
) : AndroidViewModel(application) {

    val allTransactions = repository.allTransactions

}