//
//  Res.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/2.
//

import Foundation

struct Res: Codable {
    var resId: Int
    var resName: String
    var resAddress: String
    var resLat: Double
    var resLon: Double
    var resTel: String
    var resHours: String
    var resCategoryId: Int
    var resCategoryInfo: String
    var resEnable: Bool
    var userId: Int
    var userName: String
    var modifyDate: Date
    var rating: Float?
    var distance: Float?
    var myRes: Bool?
}

struct getAllResPost: Encodable {
    let action = "getAll"
}

struct getAllResEnablePost: Encodable {
    let action = "getAllEnable"
    var userId: Int
}
