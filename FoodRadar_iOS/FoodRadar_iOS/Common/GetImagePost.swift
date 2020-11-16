//
//  GetImagePost.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/10.
//

import Foundation

struct GetImagePost: Encodable {
    let action = "getImage"
    var id: Int
    var imageSize: Int
    
}
