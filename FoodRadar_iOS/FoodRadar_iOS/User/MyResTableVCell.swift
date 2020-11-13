
//
//  MyResTableVCell.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/9.
//

import UIKit

class MyResTableVCell: UITableViewCell {
    @IBOutlet weak var ivMyRes: UIImageView!
    @IBOutlet weak var lbResName: UILabel!
    @IBOutlet weak var lbResHour: UILabel!
    @IBOutlet weak var lbResTel: UILabel!
    @IBOutlet weak var lbResAddress: UITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    // Cell內容內縮
    // Inside UITableViewCell subclass
    override func layoutSubviews() {
        super.layoutSubviews()

        contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10))

    }
    
}
