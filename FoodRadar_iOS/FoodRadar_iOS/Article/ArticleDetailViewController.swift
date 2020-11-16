//
//  ArticleDetailViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/6.
//

import UIKit

/* 宣告一類別 > 取得當前的圖片資料 **/
struct GetImage: Encodable {
    let action = "getImage"
    let id: Int
    let imageSize: Double
    
}

class ArticleDetailViewController: UIViewController, UICollectionViewDataSource, UICollectionViewDelegate {
    
    @IBOutlet weak var userIcon: UIImageView!
    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var resCategoryInfo: UILabel!
    @IBOutlet weak var articleTime: UILabel!
    @IBOutlet weak var articleTitle: UILabel!
    @IBOutlet weak var resName: UILabel!
    @IBOutlet weak var avgCon: UILabel!
    @IBOutlet weak var articleText: UITextView!
    @IBOutlet weak var articleGoodCount: UILabel!
    @IBOutlet weak var articleCommentCount: UILabel!
    @IBOutlet weak var articleFavoriteCount: UILabel!
    @IBOutlet weak var ivArticleComment: UIImageView!
    @IBOutlet weak var btArticleGood: UIButton!
    @IBOutlet weak var btArticleFavorite: UIButton!
    @IBOutlet weak var imageCollectionView: UICollectionView!
    @IBOutlet weak var imageFlowLayout: UICollectionViewFlowLayout!
    
    @IBOutlet weak var btComment: UIButton!

    @IBOutlet weak var btSetting: UIButton!
    
    var articleDetail: Article!
    var articleImage = [Image]()
    var image: Image!
    let url_image = URL(string: common_url + "ImgServlet")
    let url_userAccount = URL(string: common_url + "UserAccountServlet")
    let url_server = URL(string: common_url + "ArticleServlet")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //設定標題為 文章主題
        self.title = articleDetail.articleTitle
        // Do any additional setup after loading the view.
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(image:UIImage(systemName: "ellipsis.rectangle"),style: .plain, target: self, action: #selector(clickSetting))
        
        showArticle()
        getImage()
        getIcon()
    }
    
    /* 顯示文章資料 **/
    func showArticle () {
        userName.text = articleDetail.userName
        resCategoryInfo.text = articleDetail.resCategoryInfo
        articleTime.text = articleDetail.articleTime
        articleTitle.text = articleDetail.articleTitle
        resName.text = "店名：\(articleDetail.resName ?? "請選擇餐廳")"
        articleText.text = articleDetail.articleText
        articleGoodCount.text = String(articleDetail.articleGoodCount ?? 0)
        articleCommentCount.text = String(articleDetail.commentCount ?? 0)
        articleFavoriteCount.text = String(articleDetail.favoriteCount ?? 0)
        avgCon.text = "平均消費：\(String(articleDetail.avgCon)) /人"
        
    }
    
    /* 抓取圖片資料 **/
    @objc func getImage() {
        var requestParam = [String : Any]()
        requestParam["action"] = "getAllById"
        requestParam["articleId"] = articleDetail?.articleId
        executeTask(self.url_image!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    
                    if let result = try? JSONDecoder().decode([Image].self, from: data!) {
                        self.articleImage = result
                        DispatchQueue.main.async {
                            self.imageCollectionView.reloadData()
                        }
                    }
                }
            } else {
                print(error!.localizedDescription)
            }
        }
    }
    
    /* 抓取大頭貼 **/
    @objc func getIcon() {
        var requestParam = [String : Any]()
        requestParam["action"] = "getImage"
        requestParam["id"] = articleDetail?.userId
        requestParam["imageSize"] = userIcon.frame.width
        var userIcon: UIImage?
        executeTask(url_userAccount!, requestParam) { data, response, error in
            if error == nil {
                if data != nil {
                    userIcon = UIImage(data: data!)
                }
                if userIcon == nil {
                    userIcon = UIImage(named: "noImage.jpg")
                }
                DispatchQueue.main.async {
                    self.userIcon?.image = userIcon
                }
            } else {
                print(error!.localizedDescription)
            }
        }
    }
    
    /* collection列數 > 圖片數量 **/
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return articleImage.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ArticleImageCollectionViewCell", for: indexPath) as! ArticleImageCollectionViewCell
        /* 抓圖片 **/
        let images = articleImage[indexPath.item]
        var requestParam = [String: Any]()
        requestParam["action"] = "getImage"
        requestParam["id"] = images.imgId
        requestParam["imageSize"] = cell.frame.width * 7
        var image: UIImage?
        //先設定預設圖檔為"noImage.jpg"
        cell.articleDetailImageView.image = UIImage(named: "noImage.jpg")
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
                        cell.articleDetailImageView.image = image
                    }
                }
                
            } else {
                print(error!.localizedDescription)
            }
        }
        return cell
}
    /* 設定顯示大圖 **/
    @IBSegueAction func setBigImage(_ coder: NSCoder) -> BigImageViewController? {
      let controller = BigImageViewController(coder: coder)
        if let item = imageCollectionView.indexPathsForSelectedItems?.first?.item {
            let images = articleImage[item]
            controller?.imageId = images.imgId
    }
        return controller
    }
    
//    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
//        if segue.identifier == "CommentSugue" {
//            let comment = articleDetail.articleId
//            let controller = segue.destination as! CommentViewController
//            controller.articleId = comment
//        }
//    }
    /* 文章點讚功能 **/
    @IBAction func articleGood(_ sender: UIButton) {
        //判斷選取狀態
        sender.isSelected.toggle()  //.toggle() 反轉狀態
        sender.isSelected = true //有被選擇
        sender.isSelected = false //沒被選擇
    }
    /* 文章收藏功能 **/
    @IBAction func articleFavorite(_ sender: UIButton) {
        //判斷選取狀態
        sender.isSelected.toggle()
    }
    
    
    /* 留言區 **/
    @IBAction func setComment(_ sender: Any) {
    }
    /* 帶文章ID到Comment **/
    @IBSegueAction func setIdToComment(_ coder: NSCoder) -> CommentViewController? {
        let articleId = articleDetail.articleId
        let userId = articleDetail.userId
        let controller = CommentViewController(coder: coder)
        controller?.articleId = articleId
        controller?.userId = userId
        return controller
    }
    
    @objc func clickSetting() {
        let controller = UIAlertController(title: "文章設定", message: "請選擇", preferredStyle: .actionSheet)
        //編輯文章
        let settingAction = UIAlertAction(title: "編輯文章", style: .default) { (_) in
            self.settingArticle()
        }
        controller.addAction(settingAction)
        //刪除文章
        let deleteAction = UIAlertAction(title: "刪除文章", style: .destructive) { [self] (_) in
            self.deleteArticle()
        }
        controller.addAction(deleteAction)
        //取消
        let cancelAction = UIAlertAction(title: "取消", style: .cancel, handler: nil)
        controller.addAction(cancelAction)
        present(controller, animated: true, completion: nil)    
    }
    
    /* 跳頁 > 編輯文章方法 **/
    func settingArticle() {
        performSegue(withIdentifier: "UpdateArticleViewController", sender: nil)
    }
    
    /* 刪除文章方法 **/
    func deleteArticle() {
        let controller = UIAlertController(title: "警告", message: "你要刪除文章嗎？", preferredStyle: .alert)
        
        /* 刪除文章資料 **/
        let articleId = articleDetail.articleId
      let articleDelete = ArticleDelete(articleStatus: false, articleId: articleId)
        //確認 > 執行刪除方法
        let yesAction = UIAlertAction(title: "是的", style: .destructive) { (_) in
            var requestParam = [String : Any]()
            requestParam["action"] = "articleDelete"
            requestParam["article"] = try! String(data: JSONEncoder().encode(articleDelete), encoding: .utf8)
            executeTask(self.url_server!, requestParam) { (data, response, error) in
                if error == nil {
                    if data != nil {
                        // 將輸入資料列印出來除錯用
                        print("input: \(String(data: data!, encoding: .utf8)!)")
                        
                        if let result = String(data: data!, encoding: .utf8) {
                            if let count = Int(result) {
                                if count != 0 {
                                    DispatchQueue.main.async {
                                        self.navigationController!.popViewController(animated: true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        controller.addAction(yesAction)
        //取消
        let noAction = UIAlertAction(title: "我再想想", style: .cancel, handler: nil)
        controller.addAction(noAction)
        present(controller, animated: true, completion: nil)
    }
}

extension ArticleDetailViewController {
    
}

