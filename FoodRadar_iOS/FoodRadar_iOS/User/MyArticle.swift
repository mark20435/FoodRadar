//
//  MyArticle.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/16.
//

import Foundation

struct MyArticleGetAllByIdStruct: Encodable {
    let action = "getMyArticleCollect"
    let id: Int
}

struct MyArticleIsMeStruct: Encodable {
    let action = "getMyArticleIsMe"
    let id: Int
}

struct MyArticleMyCommentStruct: Encodable {
    let action = "getMyArticleMyComment"
    let id: Int
}

struct MyArticleGetAllByIdArray: Decodable {
    var myArticleGetAllById: MyArticleGetAllById?
}


// Date Time: 2020-11-16 17:24:17
// table name: MyArticle
struct MyArticleGetAllById: Codable {
    let articleId: Int
    let articleTitle: String
    let articleTime: Date?
    let articleText: String
    let userName: String
    let articleStatus: Bool
}

struct MyArticleMyComment: Codable {
    let articleId: Int
    let articleTitle: String    
    let commentId: Int
    let commentTime: Date?
    let commentText: String
    let userName: String
    let commentStatus: Bool
}

