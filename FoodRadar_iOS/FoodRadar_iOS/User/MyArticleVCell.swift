//
//  MyArticleVCell.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/16.
//

import UIKit

class MyArticleVCell: UITableViewCell {
    @IBOutlet weak var imageArticle: UIImageView!
    @IBOutlet weak var labelArticleTitle: UILabel!
    @IBOutlet weak var labelArticleTime: UILabel!
    @IBOutlet weak var labelUserName: UILabel!
    @IBOutlet weak var labelArticleText: UITextView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
