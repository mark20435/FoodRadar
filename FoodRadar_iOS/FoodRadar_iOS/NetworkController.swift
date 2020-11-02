//
//  NetworkController.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/2.
//

import Foundation

class NetworkController {
    
    let baseURL = URL(string: "http://localhost:8080/FoodRadar_Web")!
    
    
    static let shared = NetworkController()
    
//    func login(account: String, password: String, completion: @escaping (Member?) -> Void ) {
//
//        let url = baseURL.appendingPathComponent("jome_member/LoginServlet")
//        var request = URLRequest(url: url)
//        request.httpMethod = "POST"
//        let loginPost = LoginPost(account: account, password: password)
//        request.httpBody = try? JSONEncoder().encode(loginPost)
//        URLSession.shared.dataTask(with: request) { (data, response, error) in
//
//            if let data = data,
//               let loginResponse = try? JSONDecoder().decode(LoginResponse.self, from: data),
//               let loginMember = loginResponse.loginMember {
//                completion(loginMember)
//            } else {
//                completion(nil)
//            }
//        }.resume()
//    }
    
}
