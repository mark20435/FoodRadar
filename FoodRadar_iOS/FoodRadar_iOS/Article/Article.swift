//
//  Article.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/4.
//

import Foundation

struct Article: Codable {
    var articleId: Int?
    var articleTitle: String?
    var articleText: String?
    var articleTime: String?
    var modifyTime: String?
    var resCategoryInfo: String?
    var resId: Int
    var userId: Int
    var resName: String?
    var userName: String?
    var conAmount: Int
    var conNum: Int
    var articleStatus: Bool
    var articleGoodCount: Int?
    var commentCount: Int?
    var favoriteCount: Int?
    var articleGoodId: Int
    var articleGoodStatus: Bool 
    var articleFavoriteStatus: Bool
    var articleFavoriteId: Int
    
    /* 計算屬性 > 計算平均消費 **/
    var avgCon: Int {
        get {
            return ( conAmount / conNum )
        }
    }
}
/* 新增文章用 **/
struct ArticleInsert: Codable {
    var articleId: Int?
    var articleTitle: String?
    var articleText: String?
    var conAmount: Int?
    var conNum: Int?
    var resId: Int?
    var userId: Int?
    var articleStatus: Bool
}

/* 更新文章用 **/
struct ArticleUpdate: Codable {
    var articleText: String?
    var articleTitle: String?
    var modifyTime: String?
    var conNum: Int?
    var conAmount: Int?
    var articleId: Int?
}

/* 刪除文章用 **/
struct ArticleDelete: Codable {
    var articleStatus: Bool
    var articleId: Int?
}
/* 取得文章Array **/
struct getAllById: Encodable {
    let action = "getAllById"
    var loginUserId: Int
}



