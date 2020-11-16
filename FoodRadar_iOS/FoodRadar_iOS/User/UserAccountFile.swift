//
//  UserAccountFile.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/11.
//

import Foundation

struct UserAccountFile: Codable {
    // Date Time: 2020-11-06 15:10:08
    // table name: UserAccount
    var userId: Int
    var userPhone: String
    var userPwd: String
    var userBirth: Date?
    var userName: String
    var allowNotifi: Bool
    var isEnable: Bool
    var isAdmin: Bool
    var userAvatar: [UInt8]
    var createDate: Date?
    var modifyDate: Date?
    
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

}
