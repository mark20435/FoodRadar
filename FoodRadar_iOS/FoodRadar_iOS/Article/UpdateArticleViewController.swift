//
//  UpdateArticleViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/12.
//

import UIKit
import PhotosUI

class UpdateArticleViewController: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UITextFieldDelegate {
    
    @IBOutlet weak var tfConNum: UITextField!
    @IBOutlet weak var tfConAmount: UITextField!
    @IBOutlet weak var tfArticleTitle: UITextField!
    @IBOutlet weak var lbResName: UILabel!
    @IBOutlet weak var lbResCategoryInfo: UILabel!
    @IBOutlet weak var tvArticleText: UITextView!
    @IBOutlet weak var showImageCollectionView: UICollectionView!
    @IBOutlet weak var showImageCollectionViewFlowLayout: UICollectionViewFlowLayout!
    @IBOutlet weak var insertImageCollectionView: UICollectionView!
    @IBOutlet weak var insertImageCollectionViewFlowLayout: UICollectionViewFlowLayout!
    
    var articleDetail: Article!
    var articleImage = [Image]()
    var image: Image!
    var id: Int?
    let url_image = URL(string: common_url + "ImgServlet")
    let url_userAccount = URL(string: common_url + "UserAccountServlet")
    let url_server = URL(string: common_url + "ArticleServlet")
    var imagesInsert =  [UIImage]()   //宣告一個[UIImage] > 裝要上傳的相片
    var articleInfo: Article?   //接Detail帶來的文章資訊
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "編輯文章"
        /* 設定右上ItemButton **/
        navigationItem.rightBarButtonItem = UIBarButtonItem(image:UIImage(systemName: "checkmark.circle"),style: .plain, target: self, action: #selector(clickSend))
        
        lbResName.text = articleInfo?.resName
        lbResCategoryInfo.text = articleInfo?.resCategoryInfo
        tfArticleTitle.text = articleInfo?.articleTitle
        tvArticleText.text = articleInfo?.articleText
        tfConNum.text = String(articleInfo?.conNum ?? 0)
        tfConAmount.text = String(articleInfo?.conAmount ?? 0)
        getImage()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getImage()
    }
    /* 限制只能輸入數字 > 限制字元編碼 **/
    func textField(_ textField:UITextField, shouldChangeCharactersIn range:NSRange, replacementString string: String) -> Bool {
        //人數及消費金額
        if tfConNum == textField || tfConAmount == textField {
            let length = string.lengthOfBytes(using: String.Encoding.utf8)
            for loopIndex in 0..<length {
                let char = (string as NSString).character(at: loopIndex)
                if char < 48 { return false }
                if char > 57 { return false }
            }
        }
        return true
    }
    /* 關掉鍵盤 **/
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        tvArticleText.resignFirstResponder()
        self.view?.endEditing(false)
    }
    
    /* collectionView長度 **/
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch collectionView {
        case showImageCollectionView:
            return articleImage.count
        default:
            return imagesInsert.count == 0 ? 1 : imagesInsert.count + 1
        }
    }
    
    //collectionView內容
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch collectionView {
        case showImageCollectionView:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "\(UpdateShowImageCollectionViewCell.self)", for: indexPath) as! UpdateShowImageCollectionViewCell
            let images = articleImage[indexPath.item]
            id = images.imgId
            var requestParam = [String: Any]()
            requestParam["action"] = "getImage"
            requestParam["id"] = images.imgId
            requestParam["imageSize"] = cell.frame.width * 4.5
            var image: UIImage?
            //先設定預設圖檔為"noImage.jpg"
            cell.showImageView.image = UIImage(named: "noImage.jpg")
            //宣告cell.imageId > 取得當前imgId
            cell.imageId = images.imgId!
            executeTask(url_image!, requestParam) { (data, response, error) in
                if error == nil {
                    if data != nil {
                        image = UIImage(data: data!)
                    }
                    //判斷如果cell的id等於images的id > 才進行讀讀取圖片 > 避免重複利用
                    if cell.imageId == images.imgId {
                        if image == nil {
                            image = UIImage(named: "noImage.jpg")
                        }
                        DispatchQueue.main.async {
                            cell.showImageView.image = image
                        }
                    }
                } else {
                    print(error!.localizedDescription)
                }
            }
            // 程式碼建立長按手勢並加至label上
            //            let longPressGesture = UILongPressGestureRecognizer(target: self, action: #selector(handleLongPressGesture))
            //            cell.addGestureRecognizer(longPressGesture)
            return cell
            
        default:
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "\(UpdateInsertImageCollectionViewCell.self)", for: indexPath) as! UpdateInsertImageCollectionViewCell
            //判斷第一個item要執行的動作
            if indexPath.item == 0 {
                cell.insertImageView.image = UIImage(systemName: "camera.circle")
            } else {
                cell.insertImageView.image = imagesInsert[indexPath.item - 1]
            }
            return cell
        }
    }
    // 定義手勢觸發時呼叫的方法(長按)
    //    @objc func handleLongPressGesture(_ gesture: UILongPressGestureRecognizer, _ indexPath: IndexPath) {
    //        switch gesture.state {
    //        case .began:
    //            return
    //        default:
    //            let controller = UIAlertController(title: "警告", message: "你要刪除圖片嗎？", preferredStyle: .alert)
    //            //確認 > 執行刪除方法
    //            let yesAction = UIAlertAction(title: "刪除", style: .destructive) { (_) in
    //                var requestParam = [String : Any]()
    //                requestParam["action"] = "imgDelete"
    //                let point = self.showImageCollectionView.indexPathForItem(at: .zero)
    //                let id = self.articleImage[point!.item].imgId
    //                requestParam["imgId"] = id
    //                executeTask(self.url_image!, requestParam) { (data, response, error) in
    //                    if error == nil {
    //                        if data != nil {
    //                            // 將輸入資料列印出來除錯用
    //                            print("input: \(String(data: data!, encoding: .utf8)!)")
    //                            if let result = String(data: data!, encoding: .utf8) {
    //                                if let count = Int(result) {
    //                                    if count != 0 {
    //                                        DispatchQueue.main.async {
    //                                            self.showImageCollectionView.reloadData()
    //                                            self.articleImage.remove(at: .zero)
    //                                        }
    //                                    }
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //            controller.addAction(yesAction)
    //            //取消刪除
    //            let noAction = UIAlertAction(title: "我再想想", style: .default, handler: nil)
    //            controller.addAction(noAction)
    //            present(controller, animated: true, completion: nil)
    //        }
    //    }
    
    /* 抓取圖片資料 **/
    @objc func getImage() {
        var requestParam = [String : Any]()
        requestParam["action"] = "getAllById"
        requestParam["articleId"] = articleInfo?.articleId
        executeTask(self.url_image!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    if let result = try? JSONDecoder().decode([Image].self, from: data!) {
                        self.articleImage = result
                        DispatchQueue.main.async {
                            self.showImageCollectionView.reloadData()
                        }
                    }
                }
            } else {
                print(error!.localizedDescription)
            }
        }
    }
    /* 點擊事件 **/
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        switch collectionView {
        case showImageCollectionView:
            let controller = UIAlertController(title: "警告", message: "你要刪除圖片嗎？", preferredStyle: .alert)
            //確認 > 執行刪除方法
            let yesAction = UIAlertAction(title: "刪除", style: .destructive) { (_) in
                let images = self.articleImage[indexPath.item]
                var requestParam = [String : Any]()
                requestParam["action"] = "imgDelete"
                requestParam["imgId"] = images.imgId
                executeTask(self.url_image!, requestParam) { (data, response, error) in
                    if error == nil {
                        if data != nil {
                            // 將輸入資料列印出來除錯用
                            print("input: \(String(data: data!, encoding: .utf8)!)")
                            if let result = String(data: data!, encoding: .utf8) {
                                if let count = Int(result) {
                                    if count != 0 {
                                        DispatchQueue.main.async {
                                            self.showImageCollectionView.reloadData()
                                            self.articleImage.remove(at: indexPath.item)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            controller.addAction(yesAction)
            //取消刪除
            let noAction = UIAlertAction(title: "我再想想", style: .default, handler: nil)
            controller.addAction(noAction)
            present(controller, animated: true, completion: nil)
            
        default:
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
    }
    
    /* 送出更新文章 **/
    @objc func clickSend() {
        let articleTitle = tfArticleTitle == nil ? "" : tfArticleTitle.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        let articleText = tvArticleText == nil ? "" : tvArticleText.text.trimmingCharacters(in: .whitespacesAndNewlines)
        let conNum = Int(tfConNum.text ?? "")
        let conAmount = Int(tfConAmount.text ?? "")
        //設定時間格式，並轉為字串
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss"
        let timeString = dateFormatter.string(from: Date())
        /* 上傳更新文章資料 **/
        let articleUpdate = ArticleUpdate(articleText: articleText, articleTitle: articleTitle, modifyTime: timeString, conNum: conNum, conAmount: conAmount, articleId: articleInfo?.articleId)
        var requestParam = [String: Any]()
        /* 連線後端 **/
        requestParam["action"] = "articleUpdate"
        requestParam["article"] = try! String(data: JSONEncoder().encode(articleUpdate), encoding: .utf8)
        executeTask(self.url_server!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    if let result = String( data: data!, encoding: .utf8 ) {
                        if Int(result) != nil {
                        }
                    }
                }
            } else {
                print(error!.localizedDescription)
            }
        }
        /* 上傳圖片 **/
        if imagesInsert.count == 0 {
            self.navigationController?.popViewController(animated: true)
        } else {
            let imageInsert = Image(articleId: articleInfo?.articleId, imgId: 0)
            requestParam["action"] = "imgInsert"
            requestParam["img"] = try! String(data: JSONEncoder().encode(imageInsert), encoding: .utf8)
            for i in 0...imagesInsert.count - 1 {
                let imageByte = self.imagesInsert[i]
                requestParam["imageBase64"] = imageByte.jpegData(compressionQuality: 1.0)!.base64EncodedString()
                executeTask(self.url_image!, requestParam) { (data, response, error) in
                    if error == nil {
                        if data != nil {
                            if let result = String(data: data!, encoding: .utf8) {
                                if let count = Int(result) {
                                    DispatchQueue.main.async {
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
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
}

/* 擴展 > 選擇照片 **/
extension UpdateArticleViewController: PHPickerViewControllerDelegate {
    @available(iOS 14, *)
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        //選擇圖片後回到前一頁
        dismiss(animated: true)
        let itemProviders = results.map(\.itemProvider)
        for itemProvider in itemProviders where itemProvider.canLoadObject(ofClass: UIImage.self) {
            itemProvider.loadObject(ofClass: UIImage.self) { [weak self] (image, error) in
                guard let self = self,
                      let image = image as? UIImage else { return }
                self.imagesInsert.append(image)
            }
        }
        //執行緒相關 > async為非同步，並不保證會同步執行
        //asyncAfter(deadline: .now() + 0.5) > 延後0.5秒才執行，確保圖片能顯示
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
            self.insertImageCollectionView.reloadData()
        }
    }
    /* 收回鍵盤 **/
    func hideKeyboard() {
        tfConNum.resignFirstResponder()
        tfArticleTitle.resignFirstResponder()
        tvArticleText.resignFirstResponder()
        tfConAmount.resignFirstResponder()
    }
}
