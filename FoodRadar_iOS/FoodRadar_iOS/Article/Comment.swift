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
    var commentGoodStatus: Bool?
    var commentModifyTime: String?
    var commentStatus: Bool?
    var commentText: String?
    var userName: String?
    var commentGoodId: Int?
    var commentGoodCount: Int?
}
/* 刪除留言 **/
struct DeleteComment: Codable {
    var commentStatus: Bool?
    var commentId: Int?
}

/* 新增留言 **/
struct InsertComment: Codable {
    var commentId: Int?
    var articleId: Int?
    var userId: Int?
    var commentStatus: Bool?
    var commentText: String?
}

/* 更新留言 **/
struct UpdateComment: Codable {
    var commentId: Int?
    var commentText: String?
    var commentModifyTime: String?
}

/* 點讚 **/
struct SetCommentGood: Codable {
    var userId: Int?
    var commentId: Int?
}
