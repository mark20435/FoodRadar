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
    
    var articleDetail: Article! //前頁帶過來的articleId
    var articleShow : Article!  //後端抓的資料
    var articleImage = [Image]()
    var image: Image!
    let url_image = URL(string: common_url + "ImgServlet")
    let url_userAccount = URL(string: common_url + "UserAccountServlet")
    let url_server = URL(string: common_url + "ArticleServlet")
    /* 宣告物件包裝資料 > 帶到update **/
    var updateArticle: Article?
    var loginUserId  = COMM_USER_ID
//    var goodCountResult: Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        //只有發文的使用者才會顯示
        if loginUserId == articleDetail.userId {
            /* 設定右上ItemButton **/
            navigationItem.rightBarButtonItem = UIBarButtonItem(image:UIImage(systemName: "ellipsis.rectangle"),style: .plain, target: self, action: #selector(clickSetting))
        }
        
    }
    override func viewWillAppear(_ animated: Bool) {
            showArticle()
            getImage()
            getIcon()
            imageCollectionView.reloadData()
        
    }
    
    /* 抓取文章資料 **/
    @objc func showArticle() {
        var requestParam = [String : Any]()
        requestParam["action"] = "findById"
        requestParam["articleId"] = articleDetail?.articleId
        requestParam["loginUserId"] = loginUserId
        executeTask(url_server!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    if let result = try? JSONDecoder().decode(Article.self, from: data!) {
                        DispatchQueue.main.async {
                            self.articleShow = result
                            self.userName.text = self.articleShow.userName
                            self.resCategoryInfo.text = self.articleShow.resCategoryInfo
                            self.articleTime.text = self.articleShow.articleTime
                            self.articleTitle.text = self.articleShow.articleTitle
                            self.resName.text = "店名：\(self.articleShow.resName ?? "請選擇餐廳")"
                            self.articleText.text = self.articleShow.articleText
                            self.articleCommentCount.text = String(self.articleShow.commentCount ?? 0)
                            self.articleFavoriteCount.text = String(self.articleShow.favoriteCount ?? 0)
                            self.avgCon.text = "平均消費：\(String(self.articleShow.avgCon)) /人"
                            //設定標題為 文章主題
                            self.title = self.articleShow.articleTitle
                            /* 遊客 > 不給點讚，也顯示未點讚 **/
                            if self.loginUserId <= 0 {
                                self.articleShow.articleGoodStatus = false
                                self.btArticleGood.isSelected = false
                                self.btArticleGood.isEnabled = false
                            } else {
                                /* 不是遊客 > 判定是否有點讚 **/
                                if (self.articleShow.articleGoodStatus ) {
                                    self.articleShow.articleGoodStatus = true
                                    self.btArticleGood.isSelected = true
                                } else {
                                    self.articleShow.articleGoodStatus = false
                                    self.btArticleGood.isSelected = false
                                }
                            }
                            //初始>先取得點讚數
                            self.articleGoodCount.text = String(self.articleDetail.articleGoodCount ?? 0)
                        }
                    }
                }
            }else {
                print(error!.localizedDescription)
            }
        }
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
        requestParam["imageSize"] = cell.frame.width * 4
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
    
    /* 文章點讚功能 **/
    @IBAction func articleGood(_ sender: UIButton) {
        if (self.articleDetail.articleGoodStatus ) {
            sender.isSelected = true
            cancelGood()
        } else {
            sender.isSelected = false
            setGood()
        }
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
        let toUpdate = UIStoryboard(name: "Article", bundle: nil).instantiateViewController(withIdentifier: "UpdateArticleViewController") as! UpdateArticleViewController
        toUpdate.articleInfo = articleShow
        self.navigationController?.pushViewController(toUpdate, animated: true)
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
        let noAction = UIAlertAction(title: "我再想想", style: .default, handler: nil)
        controller.addAction(noAction)
        present(controller, animated: true, completion: nil)
    }
    
    /* 點讚方法 **/
    func setGood () {
        var requestParam = [String : Any]()
        requestParam["action"] = "articleGoodInsert"
        requestParam["articleId"] = articleDetail.articleId
        requestParam["loginUserId"] = loginUserId
        executeTask(self.url_server!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    if let result = String(data: data! , encoding: .utf8) {
                        if Int(result) != nil {
                            DispatchQueue.main.async {
                                let goodPlus = self.articleShow.articleGoodCount! + 1
                                self.articleGoodCount.text = String(goodPlus)
                                self.btArticleGood.isSelected.toggle()
                                self.articleDetail.articleGoodStatus = true
                            }
                        }
                    }
                }
            }
        }
    }
    /* 取消讚方法 **/
    func cancelGood () {
        var requestParam = [String : Any]()
        requestParam["action"] = "articleGoodDelete"
        requestParam["articleId"] = articleDetail.articleId
        requestParam["userId"] = loginUserId
        executeTask(self.url_server!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    if let result = String(data: data! , encoding: .utf8) {
                        if Int(result) != nil {
                            DispatchQueue.main.async {
                                let goodLess = self.articleShow.articleGoodCount! - 1
                                self.articleGoodCount.text = String(goodLess)
                                self.btArticleGood.isSelected.toggle()
                                self.articleDetail.articleGoodStatus = false
                            }
                        }
                    }
                }
            }
        }
    }
    
}



