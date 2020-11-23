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


struct UserAvatarStruct: Encodable {
    let action = "getImage"
    let imageSize: Int
    let id: Int
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
    
    static let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    
    static func readUsersFromFile() -> [Self]? {
        let propertyDecoder = PropertyListDecoder()
        let url = Self.documentsDirectory.appendingPathComponent("foodRadarUser")
        
        if let data = try? Data(contentsOf: url),
           let userData = try? propertyDecoder.decode([Self].self, from: data) {
            return userData
        } else {
            return nil
        }
    }
    
    
    static func saveToFile(userData: [Self]) {
        let propertyEncoder = PropertyListEncoder()
        if let data = try? propertyEncoder.encode(userData) {
           let url = Self.documentsDirectory.appendingPathComponent("foodRadarUser")
            try? data.write(to: url)
        }
    }
    
    static func removeFromFile() {
        
        let url = Self.documentsDirectory.appendingPathComponent("foodRadarUser")
        try! FileManager.default.removeItem(at: url)
        
    }
    
}

