//
//  UpdateArticleViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/12.
//

import UIKit

class UpdateArticleViewController: UIViewController {
    @IBOutlet weak var tfConNum: UITextField!
    @IBOutlet weak var tfConAmount: UITextField!
    @IBOutlet weak var tfArticleTitle: UITextField!
    @IBOutlet weak var lbResName: UILabel!
    @IBOutlet weak var lbResCategoryInfo: UILabel!
    @IBOutlet weak var tvArticleText: UITextView!
    @IBOutlet weak var imageCollectionViewFlowLayout: UICollectionViewFlowLayout!
    @IBOutlet weak var insertImageCollectionViewController: UICollectionView!
    @IBOutlet weak var insertImageViewCollectionViewFlowLayout: UICollectionViewFlowLayout!
    @IBOutlet weak var imageCollectionViewController: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "編輯文章"
        // Do any additional setup after loading the view.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
