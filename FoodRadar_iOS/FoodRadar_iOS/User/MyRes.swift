//
//  MyRes.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/11.
//

import Foundation

struct MyResStruct: Encodable {
    let action = "getAllById"
    let id: Int // userId
}

// 要呈現在畫面上的餐廳資料
struct MyRes: Codable {
    let resId: Int
    let resName: String
    let resHours: String
    let resTel: String
    let resAddress: String
    let resImg: [UInt8]?
}


struct MyResGetImageStruct: Encodable {
    let action = "getImage"
    var id: Int
    var imageSize: Int
    
}
