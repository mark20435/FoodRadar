//
//  ArticleTableViewCell.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/4.
//

import UIKit

class ArticleTableViewCell: UITableViewCell {
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
    /* 解決重複利用 **/
    var task: URLSessionDataTask?
    override func prepareForReuse() {
        super.prepareForReuse() //重複利用之前呼叫
        task?.cancel()  //先清除，以便重新reload
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
