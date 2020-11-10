//
//  CommentTableViewCell.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/9.
//

import UIKit

class CommentTableViewCell: UITableViewCell {
    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var userIcon: UIImageView!
    @IBOutlet weak var commentText: UILabel!
    @IBOutlet weak var commentGoodIcon: UIButton!
    @IBOutlet weak var commentGoodCount: UILabel!
    @IBOutlet weak var commentTime: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
