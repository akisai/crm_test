package com.example.haimin_a.crm_test.rest_client

enum class Operations(val str: String) {
    save("/save"),
    findUser("/findUser"),
    userInfo("/userInfo"),
    updateInfo("/updateInfo"),
    getDoctors("/getDoctors"),
    getProcedure("/getProcedure"),
    getTime("/getTime"),
    getRasp("/getRasp"),
    saveRasp("/saveRasp"),
    getMyRasp("/getMyRasp"),
    deleteRasp("/deleteRasp"),
    deleteAcc("/deleteAcc")
}