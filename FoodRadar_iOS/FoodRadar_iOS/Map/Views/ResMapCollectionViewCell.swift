//
//  ResMapCollectionViewCell.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/11.
//

import UIKit

class ResMapCollectionViewCell: UICollectionViewCell {

    @IBOutlet weak var ivRes: UIImageView!
    @IBOutlet weak var lbResName: UILabel!
    @IBOutlet weak var lbResAddress: UILabel!
    @IBOutlet weak var lbResCategoryInfo: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
