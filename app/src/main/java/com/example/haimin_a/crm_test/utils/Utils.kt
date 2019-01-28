package com.example.haimin_a.crm_test.utils

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.net.HttpURLConnection
import java.net.URL

private val ERROR = "error"

fun buildPostParams(connection: HttpURLConnection, method: String = "POST") {
    connection.requestMethod = method
    connection.connectTimeout = 30000
    connection.readTimeout = 30000
    connection.setRequestProperty("Content-Type", "application/json")
}

fun getPostResponse(url: String, json: String): String {
    val response: String?
    try {
        val connection = URL(url)
            .openConnection() as HttpURLConnection
        buildPostParams(connection)
        connection.outputStream.write(json.toByteArray())
        response = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
    } catch (e: Throwable) {
        return ERROR
    }
    return response
}

fun getGetResponse(url: String): String {
    val response: String?
    try {
        val connection = URL(url)
            .openConnection() as HttpURLConnection
        buildPostParams(connection, "GET")
        response = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
    } catch (e: Throwable) {
        return ERROR
    }
    return response
}

fun processingResponse(
    context: Context,
    response: String,
    titleAlert: String = "Error",
    message2: String = "Unexpected error",
    message1: String = "Connection error",
    needFirst: Boolean = true,
    needSecond: Boolean = true
): Boolean {
    if (response == ERROR) {
        if (needFirst)
            context.alert(message1) {
                title = titleAlert
                yesButton {}
            }.show()
    } else {
        if (response.isNotEmpty()) {
            return true
        } else if (needSecond)
            context.alert(message2) {
                title = titleAlert
                yesButton {}
            }.show()
    }
    return false
}


// from 59.9881 30.2166
fun getPoints(): MutableList<List<LatLng>> {
    val path: MutableList<List<LatLng>> = ArrayList()
    path.add(PolyUtil.decode("ilcmJ{vlwDgB_AIEcAw@"))
    path.add(PolyUtil.decode("_rcmJyzlwDSnCM`B"))
    path.add(PolyUtil.decode("ascmJgslwDXLb@NPHbDrAjAf@l@X"))
    path.add(PolyUtil.decode("sgcmJimlwDZLRFJ_BNwBdCs_@Ba@jAcQV{DBUNgC\\_FN}B\\uFFaA"))
    path.add(PolyUtil.decode("qzbmJufowDXmEJaBHeAVkDLyBJqC@c@HmEFoA`@cGRcEDiBBuA?e@@]Ag@Cg@GuAKsBCq@Aa@?y@?e@BgE?wA@U?Y"))
    path.add(PolyUtil.decode("submJoyqwDjAHRdABJDP@^"))
    path.add(PolyUtil.decode("grbmJauqwDGtDOpEe@tMMfCQpC[tEa@~FOdCQfC"))
    path.add(PolyUtil.decode("exbmJezowD[`DSdAKd@M`@O^SXKNURc@RUD[DcAAEAs@Q]EeBWeA@w@Pg@Ig@Is@MqDa@qF}@_Fo@eA@_@?_@AmAAsDN}BL}CRwC?aAAeAQaBW"))
    path.add(PolyUtil.decode("ydemJesowDu@c@SGuB]m@Mm@MGCgE}@WGo@Mg@IuAUqBWYEEAg@Go@AaBI"))
    path.add(PolyUtil.decode("wdfmJw{owDMDKBK?u@?"))
    path.add(PolyUtil.decode("sgfmJm{owDEFCHAJ?J?RBX@LBHDBFDTBX@JFJHp@Fd@Bl@H`@Dn@JrARtBXh@L`@Fh@FjCh@NDd@Rd@Fd@DlBXr@Bb@@j@HNAJ?FAFCj@Uj@Bb@?|A@l@BbA@bCMj@ElBOfEEb@?N?L?\\@d@?bBRhKxApB^rAPl@LlFv@jCb@l@HhB^"))
    path.add(PolyUtil.decode("o}bmJqgowDJ@LBt@Ht@Lz@NjCj@v@Xt@\\x@`@x@l@t@l@p@v@t@~@nBdDdA|Bb@hAj@bBv@vCl@pCz@pE`AvFvB|Ld@dCZzAh@fCt@fCb@tAf@nAj@pAd@~@l@hA\\d@j@v@\\d@n@v@l@f@v@l@p@f@l@\\d@PZJZJZHZHZF\\F\\B`@Bx@@x@Cx@Kv@Op@SZKZKv@e@hGwEhB{ArAy@r@_@f@S`@Mh@KrAM~QwAnJ{@v@@n@Bl@Ld@J|Aj@`Ap@^V`@^bApAl@dAr@pAx@zAhCzFtCvFbAzAdA|An@v@dAjA`Ar@pBrAlDbBtCjAfIbElFfCfFjCvKpFxDjBbDpA`Cn@dDZrB?tCM~@Ot@OlBw@xBcAfBkAvAgAdIiHdIuHp@s@nBeBrB_BtA}@n@c@lAm@|Ak@|Cw@vCc@fBMXARCtCUpBMDAT?rAAdAEjAChF]`COzBQnESjDQrCOfC?hDNxCXzCZpDf@xEf@zEn@xHx@~Ej@fFr@XDbC\\hB^n@NzBx@`B|@|@n@t@d@fA|@lC~BvBfBpA|@\\TTHt@XPDx@Vz@LzAFvACfBc@dAWfAi@`BiA~AeBhAuAtAwBbBiDhAiD|@qDbAiE|@wD~DePtAkFbAiEr@eCh@eBbG}SdByGzAeHd@wCl@mDrAqGpAkF|AcFj@oB`CaJzAgFh@gBj@qBRy@tAqFt@iDZcBTuALwAR{Bf@cH`@sFZyEHcAPwAZ}B\\aC\\qBr@eDBKh@}Al@}AVc@Zy@d@aA`@w@j@cA~AmBjBgBtB_BvD}DxBuC`B{DhBoFpAoF|BkMz@mFzBiMnAkGnBmMVwAVwAfBwKd@wCb@oCt@cFNgAJw@L{@n@{EXmB`@}CRkBRoBJmAJsAReDJsBHmBHqBDsAHmAJuBVaDVaDdAqLf@uGDu@P{BJsAXiDV}BZiC`@cCHe@TkAPs@Je@@GBG@GBGHYNk@Rm@b@qAN]l@yAj@mAbAcBDKbAqAHGV[JKX[fA}@^UPMNI\\OZO\\ORGp@IfAGl@?l@EZCZAh@E|@Y^Gh@Cv@?~@@h@DpBZ~Gh@pDTHBfCZrAN|Cn@hBj@lA^`Ct@pAr@v@TnCf@tAXbBZdARj@Jj@JRDzAVdDf@~ATjALdDXbERb@@b@@jA@@?v@@v@@dCDbBHrJp@J@^Dd@BnCVn@FnBZhBZdATVFTFJBlA\\VF@@XHfA\\~@\\nBt@`CdAnBbAhB`ApBtA`@XhCfBjBzAbAx@h@d@RNVVf@f@f@`@tBtBRRlAnAxAnA~BjB|AjARLfCdB~@j@l@\\NH`Ah@h@Xp@ZRJf@T~B~@xE|A`AZx@Vv@Tr@RzC`ARFjCx@"))
    path.add(PolyUtil.decode("_welJibywDr@Df@NPF~@Xd@Nr@X`@P\\R`@Zh@f@`@h@^j@NX`@x@Xr@`@pAXfAP~@Fn@Bt@?t@Cl@G`@Mh@ENYh@QPUNYDO?WISMQWQ_@Ma@Ic@Gg@Co@?{@DmA@o@@k@CWE]b@eEd@gDn@mDBSLo@^_B\\wAXaAZgAx@eC|@wBv@eB^y@HMP[JQZg@l@{@l@{@z@eAl@s@n@s@lEuE|AeBNO`AmAfAuAx@iAz@oAJOz@qAp@iAnA{Bt@sAzAyCf@eAz@mBp@{Af@mAtRwf@rAmDtAmDvJ_WfKyWHSvBsF~DkKd@iAf@oA`BcEd@mAjBwEhByEv@qBpAoDn@oBf@_BPm@`@_Bl@aCLe@H]BKVqABQDOBOXyATuAZuBJs@f@yD@KPeBFe@LkAL{AHeAPsCFmAFiAHeCDqAF}BB}BBaC?qB?sBAaDCuCEmBKiCK_COsCO_COmBGy@UeCOmAKs@]_C]aCe@cCa@oBa@gBWmAa@eB_AoDs@aC{@uCWaAYeA[kAQs@S}@]}Aq@kDk@eD]uBSyAUeBm@gFMwAUoCQyCMuBAg@OoCOsDIuCA}@Cy@CcBCgC?_C?uA@kEFqEPuLDqEBeC@cB?oAA{AA}DQkIIgDGsBSiFAYAGEkA?K]}GkA{Vi@uKE}@i@cLGy@WqGUaFW}FY}F"))
    path.add(PolyUtil.decode("w}alJognxDUaFa@kI]_H_@mIUcFa@cI[uGSmDYyEMgBMoBYqDa@yEMmAKmAm@gG]cD]wCa@cDy@yGeAaHg@{Do@gEEYsAmJiE_[sB{NeAuHmAyI_AyGKo@cAmHCS]eCWmBmAgIy@iFc@wBa@eBi@{B[cAW_AWy@u@}B}@eCGMKUQa@KUcAuBkAyBiAkBOSm@_A}@sAwAwB_@m@w@kAmCcEu@kAm@{@kC_EW_@kEaG_CcDcDmE_@o@}@yA]m@eDwE[e@qBuCWc@wEuG{CuDm@q@iAsAkBqBwBsBs@o@c@_@eA}@u@m@yB_B]WaAo@kAu@sAu@kBcAy@a@c@Um@YgCsAqAq@w@e@mA{@][y@u@eAeAy@_ASYq@aAwA}BaAeBm@oA_@}@Ys@a@gA[}@o@uBiAoE}@gEo@iD}@_FmCoNiCkNQ_AsAaHoBaK"))
    path.add(PolyUtil.decode("a}hlJ{g_yDCqB_@aEScCIeAG}@Ew@Ai@?g@?c@Ds@Dg@Fa@Jk@Ha@Le@HUHSLWf@o@LMTMTKXIVGTCJAF?HBJDJHNNJNFJJPHP@BJVFNHNHHJFNBTBRP"))
    path.add(PolyUtil.decode("ophlJ}e`yDZGl@Kj@Ij@ONGLKTYR]Rk@f@}A`@oAHQHSJONQNMPILG\\K~C}@"))
    path.add(PolyUtil.decode("i}glJcv`yD|F_B`B_@bBc@pCq@bDy@fAWn@MLE"))
    path.add(PolyUtil.decode("iaglJ_`ayDZIrCw@~@WZIhAWb@MPE^In@IRO"))
    path.add(PolyUtil.decode("yqflJseayDZGBAPGPIRKPQPWJMRe@Zy@Tq@dBuFbAuCl@oBxCiJd@yAL]Vy@Lm@Lm@Pw@R_An@wCn@{CzAyGv@uDJc@d@wBt@iD`@oBH_@p@{CLm@Je@"))
    path.add(PolyUtil.decode("wlelJ{bdyDbEAxBBV?x@G"))
    path.add(PolyUtil.decode("g`elJacdyDIVKReAzDe@~A}@|COj@yBlIk@|C"))
    path.add(PolyUtil.decode("}kelJcccyDfAlB^j@NR@BV^^f@t@~@h@`@"))
    path.add(PolyUtil.decode("kcelJgxbyD`@oAf@}B\\mAdAkD^oAdA{D|D{NlA_FBGBIDGDCJAHBLHx@hA~BdDt@~@fAjAv@^"))
    path.add(PolyUtil.decode("cedlJmycyDtAuEh@iBpDwLhEiNrD_M`@oAN_@Vk@P]NWLQ^]XQHGHELELCTEXAx@?|@@D@jPb@D@"))
    path.add(PolyUtil.decode("oqblJyveyDACcHsNsCcGu@yAWk@CGAG?C"))
    return path
}


