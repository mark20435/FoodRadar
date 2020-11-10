//
//  ArticleCollectionViewCell.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/6.
//

import UIKit

class ArticleCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var userIcon: UIImageView!
    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var resCategoryInfo: UILabel!
    @IBOutlet weak var articleTitle: UILabel!
    @IBOutlet weak var resName: UILabel!
    @IBOutlet weak var lbArticleTime: UILabel!
    @IBOutlet weak var lbgoodCount: UILabel!
    @IBOutlet weak var ivGoodIcon: UIImageView!
    @IBOutlet weak var lbCommentCount: UILabel!
    @IBOutlet weak var ivArticleCommentIcon: UIImageView!
    @IBOutlet weak var lbFavoriteArticle: UILabel!
    @IBOutlet weak var ivFavoriteIcon: UIImageView!
    @IBOutlet weak var ivArticleImage: UIImageView!
    

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
//    
//    func collectionView(UICollectionView: , didSelectItemAt: IndexPath) {
//        
//    }


}
