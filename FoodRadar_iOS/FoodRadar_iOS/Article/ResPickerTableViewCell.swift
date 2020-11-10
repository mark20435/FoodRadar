//
//  ResPickerTableViewCell.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/8.
//

import UIKit

class ResPickerTableViewCell: UITableViewCell {
    @IBOutlet weak var lbResName: UILabel!
    @IBOutlet weak var lbResAddress: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
