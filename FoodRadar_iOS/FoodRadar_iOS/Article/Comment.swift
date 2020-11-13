//
//  Comment.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/9.
//

import Foundation

struct Comment: Codable {
    var commentId: Int?
    var commentTime: String?
    var articleId: Int?
    var userId: Int?
    var commentGoosStatus: Bool?
    var commentModifyTime: String?
    var commentStatus: Bool?
    var commentText: String?
    var userName: String?
    var commentGoodId: Int?
    var commentGoodCount: Int?
}
