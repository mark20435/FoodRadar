//
//  InsertArticleViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/8.
//

import UIKit
import PhotosUI

class InsertArticleViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UITextFieldDelegate {
    
    
    @IBOutlet weak var tfConMum: UITextField!
    @IBOutlet weak var tfConAmount: UITextField!
    @IBOutlet weak var tfArticleTitle: UITextField!
    @IBOutlet weak var lbResName: UILabel!
    @IBOutlet weak var lbResCategoryInfo: UILabel!
    @IBOutlet weak var tvArticleText: UITextView!
    @IBOutlet weak var InsertImageCollectionViewController: UICollectionView!
    @IBOutlet weak var UICollectionViewFlowLayout: UICollectionViewFlowLayout!
    
    var articleDetail: Article?
    var imageArray = [Image]()
    var image: Image?
    var images =  [UIImage]()   //宣告一個[UIImage] > 裝要上傳的相片
    var resAddress: ResAddress?
    var resId: Int? //宣告resId > 裝帶回的餐廳Id
    var imageView: UIImage?
    
    let url_article = URL(string: common_url + "ArticleServlet")
    let url_image = URL(string: common_url + "ImgServlet")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "發文"
        
        tfConMum.delegate = self
        tfConAmount.delegate = self
    }
    
    /* 限制格式為數字格式 > 限制字元編碼 **/
    func textField(_ textField:UITextField, shouldChangeCharactersIn range:NSRange, replacementString string: String) -> Bool {
        //人數及消費金額
        if tfConMum == textField || tfConAmount == textField {
            let length = string.lengthOfBytes(using: String.Encoding.utf8)
            for loopIndex in 0..<length {
                let char = (string as NSString).character(at: loopIndex)
                if char < 48 { return false }
                if char > 57 { return false }
            }
        }
     return true
    }
    
    /* collectionView長度 **/
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return images.count == 0 ? 1 : images.count + 1
    }
    
    /* collectionView設定 **/
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "\(InsertImageCollectionViewCell.self)", for: indexPath) as! InsertImageCollectionViewCell
        //判斷第一個item要執行的動作
        if indexPath.item == 0 {
            cell.imageview.image = UIImage(systemName: "camera.circle")
      
        } else {
            cell.imageview.image = images[indexPath.item - 1]
            
    }
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if indexPath.item == 0 {
            //點擊時選擇照片
            if #available(iOS 14, *) {
                var configuration = PHPickerConfiguration()
                //configuration.filter預設可選擇圖片及影片(nil) > .images只選圖片
                configuration.filter = .images
                configuration.selectionLimit = 0    //0 > 不限制選擇張數
                let pick = PHPickerViewController(configuration: configuration)
                pick.delegate = self
                //顯示當下所執行的任務
                present(pick, animated: true)
            }
        }
    }
    /* 上傳文章 **/
    @IBAction func insertArticle(_ sender: Any) {
        let articleTitle = tfArticleTitle == nil ? "" : tfArticleTitle.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        let articleText = tvArticleText == nil ? "" : tvArticleText.text.trimmingCharacters(in: .whitespacesAndNewlines)
        let conNum = Int(tfConMum.text ?? "")
        let conAmount = Int(tfConAmount.text ?? "")

        /* 上傳文章資料>包成類別 **/
      let articleInsert = ArticleInsert(articleId: 0, articleTitle: articleTitle, articleText: articleText, conAmount: conAmount, conNum: conNum, resId: resId, userId: 5, articleStatus: true)
        
        var requestParam = [String: Any]()
        
        /* 連線後端 **/
        requestParam["action"] = "articleInsert"
        requestParam["article"] = try! String(data: JSONEncoder().encode(articleInsert), encoding: .utf8)
        executeTask(self.url_article!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    if let result = String( data: data!, encoding: .utf8 ) {
                        if Int(result) != nil {
//                            DispatchQueue.main.async {
                                //新增成功返回前一頁
//                                if count != 0 {
//                                    self.navigationController?.popViewController(animated: true)
//                                } else {
//                                    //新增失敗時動作
//                                    return
//                                }
//                            }
                        }
                    }
                }
            } else {
                print(error!.localizedDescription)
            }
        }
        
        /* 上傳圖片 **/
        let imageInsert = Image(articleId: 0, imgId: 0)
        
        requestParam["action"] = "findByIdMax"
        requestParam["img"] = try! String(data: JSONEncoder().encode(imageInsert), encoding: .utf8)

            for i in 0...images.count - 1 {
               let imageByte = self.images[i]
                requestParam["imageBase64"] = imageByte.jpegData(compressionQuality: 1.0)!.base64EncodedString()
                executeTask(self.url_image!, requestParam) { (data, response, error) in
                    if error == nil {
                        if data != nil {
                            if let result = String(data: data!, encoding: .utf8) {
                                if let count = Int(result) {
                                    DispatchQueue.main.async {
                                        // 新增成功則回前頁
                                        if count != 0 {                                            self.navigationController?.popViewController(animated: true)
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
        
    }

}

/* 擴展 > 選擇照片 **/
extension InsertArticleViewController: PHPickerViewControllerDelegate {
    @available(iOS 14, *)
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        //選擇圖片後回到前一頁
        dismiss(animated: true)
        
        let itemProviders = results.map(\.itemProvider)
        for itemProvider in itemProviders where itemProvider.canLoadObject(ofClass: UIImage.self) {
            itemProvider.loadObject(ofClass: UIImage.self) { [weak self] (image, error) in
                guard let self = self,
                      let image = image as? UIImage else { return }
                self.images.append(image)
            }
        }
        //執行緒相關 > async為非同步，並不保證會同步執行
        //asyncAfter(deadline: .now() + 0.5) > 延後0.5秒才執行，確保圖片能顯示
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            self.InsertImageCollectionViewController.reloadData()
        }
    }
    
    /* step1-1.要先在第一頁宣告unwindSegue方法() **/
    /* 要加 @IBAction > 但先不用關聯UI元件 由第二頁的cell拉線到exit關聯 **/
    @IBAction func unwindToInsertArticleViewController(_ unwindSegue: UIStoryboardSegue) {
        /* step1-2.宣告物件 > unwindSegue的資料，轉型為第二頁的Controller **/
        if let sourceViewController = unwindSegue.source as? ResPickerTableViewController {
            //接收 第二頁回傳的資料 selectRes>第二頁宣告的物件
            lbResName.text = sourceViewController.selectRes?.resName
            lbResCategoryInfo.text = sourceViewController.selectRes?.resCategoryInfo
            resId = sourceViewController.selectRes?.resId
        }
    }
}


