package com.dscvit.notix.receivers

/*
@AndroidEntryPoint
class MySMSBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: NotixRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        var body = ""
        val bundle = intent?.extras
        val pdusArr = bundle!!.get("pdus") as Array<*>
        val messages: Array<SmsMessage?> = arrayOfNulls(pdusArr.size)

        // if SMS is Long and contain more than 1 Message we'll read all of them
        for (i in pdusArr.indices) {
            messages[i] = SmsMessage.createFromPdu(pdusArr[i] as ByteArray)
        }
        var MobileNumber: String? = messages[0]?.originatingAddress
        Log.i(ContentValues.TAG, "MobileNumber =$MobileNumber")
        val bodyText = StringBuilder()
        for (i in messages.indices) {
            bodyText.append(messages[i]?.messageBody)
        }
        body = bodyText.toString()
        if (body.isNotEmpty()) {
            // Do something, save SMS in DB or variable , static object or ....
            Log.i("Inside Receiver :", "body =$body")
        }


        parseSMS(body)
    }



    private fun parseSMS(body: String) {
        var flag = true
        Log.d("THE FLAG BEFORE", flag.toString())
        val dateNow = Calendar.getInstance().time
        val tempData = TransactionData("", "1000", dateNow.toString())
        if (body.contains("credited")) {
            tempData.type = "credited"
        } else if (body.contains("debited")) {
            tempData.type = "debited"
        } else {
            flag = false
        }
        var test = "watching tv (at home)"
        test = test.replace("\\p{P}","")

        */
/*val merchantNameRegEx = Pattern.compile("(?i)(?:\\sat\\s|in\\*)([A-Za-z0-9]*\\s?-?\\s?[A-Za-z0-9]*\\s?-?\\.?)")
        val merchant: Matcher = merchantNameRegEx.matcher(body)
        Log.d("MERCHANT NAME", merchant.group(0))*//*

        //extractMerchantNameFromSMS(body)
        //val regEx: Pattern = Pattern.compile("[rR][sS]\\.?\\s[,\\d]+\\.?\\d{0,2}|[iI][nN][rR]\\.?\\s*[,\\d]+\\.?\\d{0,2}")
        //val regEx: Pattern = Pattern.compile("[rR][sS](\\\\s*.\\\\s*\\\\d*)")
        // Find instance of pattern matches
        */
/*val m: Matcher = regEx.matcher(body)
        Log.d("amount_value= ", "" + m)
        var amount = m.group(0).replace("inr".toRegex(), "")
        amount = amount.replace("rs".toRegex(), "")
        amount = amount.replace("inr".toRegex(), "")
        amount = amount.replace(" ".toRegex(), "")
        amount = amount.replace(",".toRegex(), "")*//*

        //Log.d("AMOUNT ", amount)

        Log.d("THE FLAG", flag.toString())
        if (flag) {
            runBlocking {
                repository.insertTransaction(tempData)
            }
        }
    }

    private fun extractMerchantNameFromSMS(body: String) {
        val TAG = "MERCHANT NAME "
        try {
            val regEx =
                Pattern.compile("(?i)(?:\\sInfo.\\s*)([A-Za-z0-9*]*\\s?-?\\s?[A-Za-z0-9*]*\\s?-?\\.?)")
            // Find instance of pattern matches
            val m = regEx.matcher(body)
            if (m.find()) {
                var mMerchantName = m.group()
                mMerchantName =
                    mMerchantName.replace("^\\s+|\\s+$".toRegex(), "") //trim from start and end
                mMerchantName = mMerchantName.replace("Info.", "")
                Log.d(TAG, "MERCHANT NAME : $mMerchantName")
            } else {
                Log.d(TAG, "MATCH NOTFOUND")
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

}*/
