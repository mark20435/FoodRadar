//
//  UserCommon.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/6.
//

import Foundation

let urlUserServlet = "UserAccountServlet"
var COMM_USER_ID = 0

func Login(userPhone: String, userPwd: String , completion: @escaping (Int?) -> Void) {
//    let url = URL(string: "http://localhost:8080/FoodRadar_Web/UserAccountServlet")
    
    var userId = 0
    
    let url = NetworkController().baseURL.appendingPathComponent(urlUserServlet)

    var request = URLRequest(url: url)
    let userLoginStruct = UserLoginStruct(userPhone: userPhone, userPwd: userPwd)
    print("userLoginStruct",userLoginStruct)
    
    request.httpMethod = "POST"
    request.httpBody = try? JSONEncoder().encode(userLoginStruct)
    URLSession.shared.dataTask(with: request) { (data, response, error) in
        let decoder = JSONDecoder()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        decoder.dateDecodingStrategy = .formatted(dateFormatter)
        if let data = data,
           let userAccountArray = try? decoder.decode([UserAccount].self, from: data) {
//            if let userAccount = userAccountArray[0] {
            userId = userAccountArray[0].userId
            print("userId: \(userId)")
            print("userName: \(userAccountArray[0].userName)")
            COMM_USER_ID = userId
            print("login USED_ID: \(COMM_USER_ID)")
            UserAccount.saveToFile(userData: userAccountArray)
            
            completion(userId)
        } else {
            completion(0)
        }
    }.resume()
}

func LogOut() {

    COMM_USER_ID = 0
    UserAccount.removeFromFile()
}


