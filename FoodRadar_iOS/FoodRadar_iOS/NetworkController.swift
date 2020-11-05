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
    
    func getAllRes(completion: @escaping ([Res]?) -> Void) {
        let url = baseURL.appendingPathComponent("ResServlet")
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.httpBody = try? JSONEncoder().encode(getAllResPost())
        URLSession.shared.dataTask(with: request) { (data, response, error) in
            let decoder = JSONDecoder()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "MMM dd, yyyy HH:mm:ss a"
            decoder.dateDecodingStrategy = .formatted(dateFormatter)
            if let data = data,
               let ress = try? decoder.decode([Res].self, from: data) {
                completion(ress)
            } else {
                completion(nil)
            }
        }.resume()
    }
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
