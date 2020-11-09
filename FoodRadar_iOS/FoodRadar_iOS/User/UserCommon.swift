//
//  UserCommon.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/6.
//

import Foundation

let urlUserServlet = "UserAccountServlet"

func login(userPhone: String, userPwd: String) -> Int {
//    let url = URL(string: "http://localhost:8080/FoodRadar_Web/UserAccountServlet")
    
    var userId = 0
    
    let url = NetworkController().baseURL.appendingPathComponent(urlUserServlet)

    var request = URLRequest(url: url)
    let userLoginStruct = UserLoginStruct(userPhone: userPhone, userPwd: userPwd)
    
    request.httpMethod = "POST"
    request.httpBody = try? JSONEncoder().encode(userLoginStruct)
    URLSession.shared.dataTask(with: request) { (data, response, error) in
        let decoder = JSONDecoder()
        let dateFormatter = DateFormatter()
//        dateFormatter.dateFormat = "MMM dd, yyyy HH:mm:ss a"
//        dateFormatter.dateFormat = "yyyy-MM-dd  hh:mm:ss"
        dateFormatter.calendar = Calendar(identifier: .iso8601)
        decoder.dateDecodingStrategy = .formatted(dateFormatter)
        if let data = data,
           let userAccountArray = try? decoder.decode([UserAccount].self, from: data) {
//            if let userAccount = userAccountArray[0] {
            userId = userAccountArray[0].userId
//            print(userAccount.userId, userAccount.userName)
            print("userId: \(userId)")
//            }
        }
    }.resume()
    return userId
}


func aaa () {
//    //        let url = URL(string: "http://localhost:8080/FoodRadar_Web/UserAccountServlet")
//
//            let url = NetworkController().baseURL.appendingPathComponent(urlUserServlet)
//
//            var request = URLRequest(url: url)
//            request.httpMethod = "POST"
//            let userAccount = UserAccount(allowNotifi: false, userBirth: Date(), userId: 0, userName: "joy", userPhone: "0931234568", userPwd: "123456")
//            let registerPost = RegisterPost(userAccount: userAccount, imageBase64: image)
//            request.httpBody = try? JSONEncoder().encode(registerPost)
//
//
//            URLSession.shared.dataTask(with: request) { (data, response, error) in
//
//                if let data = data,
//                   String(data: data, encoding: .utf8) == "1" {
//                    print("ok")
//                } else {
//                    print("error")
//                }
//
//            }.resume()
}
