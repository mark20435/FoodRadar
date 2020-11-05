//
//  Article.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/4.
//

import Foundation

struct Article: Codable {
    var articleId: Int
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
    var articleImg: Data
    var userIcon: Data
    var articleFavoriteId: Int
}

/* 取得文章資料(多篇文章) **/
struct getAllById: Encodable {
    let action = "getAllById"

}
