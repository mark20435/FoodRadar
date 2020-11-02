//
//  ResCell.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/2.
//

import UIKit

class ResCell: UITableViewCell {
    @IBOutlet weak var ivRes: UIImageView!
    @IBOutlet weak var lbResName: UILabel!
    @IBOutlet weak var lbResAddress: UILabel!
    @IBOutlet weak var lbResCategoryInfo: UILabel!
    
    @IBOutlet weak var lbResTel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
