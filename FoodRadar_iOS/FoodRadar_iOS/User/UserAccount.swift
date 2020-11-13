//
//  UserStruct.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/6.
//

import Foundation


struct UserLoginStruct: Encodable {
    let action = "userLogin"
    let userPhone: String
    let userPwd: String
}


struct UserAccountArray: Decodable {
    var userAccount: UserAccount?
}


struct UserAccount: Codable {
    // Date Time: 2020-11-06 15:10:08
    // table name: UserAccount
    let userId: Int
    let userPhone: String
    let userPwd: String
    let userBirth: Date?
    let userName: String
    let allowNotifi: Bool
    let isEnable: Bool
    let isAdmin: Bool
    let userAvatar: [UInt8]
    let createDate: Date?
    let modifyDate: Date?
}
