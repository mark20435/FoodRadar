//
//  RankCollectionViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/6.
//

import UIKit

class RankTableViewController: UITableViewController {
    
    var allArticle = [Article]()
    let url_server = URL(string: common_url + "ArticleServlet")
    let url_userAccount = URL(string: common_url + "UserAccountServlet")
    let url_image = URL(string: common_url + "ImgServlet")

    override func viewDidLoad() {
        super.viewDidLoad()
        /* 連接到xib的Cell **/
        self.tableView.register(UINib(nibName: "ArticleTableViewCell", bundle: nil), forCellReuseIdentifier: "ArticleTableViewCell")

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Do any additional setup after loading the view.
    }
    /* 顯示抓取的資料 **/
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if let selectedIndexPath = tableView.indexPathForSelectedRow {
            tableView.deselectRow(at: selectedIndexPath, animated: animated)
        }
        
        showAllArticles()
    }
    
    /* 抓取文章的方法 **/
    @objc func showAllArticles() {
        var requestParam = [String : Any]()
        requestParam["action"] = "getAllByIdRank"
        requestParam["loginUserId"] = 0 //先寫死(遊客)
        executeTask(self.url_server!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    if let result = try? JSONDecoder().decode([Article].self, from: data!) {
                        self.allArticle = result
                        DispatchQueue.main.async {
                            self.tableView.reloadData()
                        }
                    }
                }
            } else {
                print(error!.localizedDescription)
            }
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    /* tableView長度限制 */
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return allArticle.count
    }
    
    /* 顯示xib到tableView上 **/
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ArticleTableViewCell") as! ArticleTableViewCell
        
        let article = allArticle[indexPath.row]
        /* 取得餐廳圖片 **/
        var requestParam = [String: Any]()
        requestParam["action"] = "getImageByArticleId"
        requestParam["id"] = article.articleId
        //圖片寬度為tableViewCell的1/4，ImageView的寬度也建議在storyboard加上比例設定的constraint
        requestParam["imageSize"] = cell.frame.width
        //先預設圖檔為noImage.jpg > 防止重複利用
        cell.ivArticleImage.image = UIImage(named: "noImage.jpg")
        var image: UIImage?
        executeTask(url_image!, requestParam) { data, response, error in
            if error == nil {
                if data != nil {
                    image = UIImage(data: data!)
                }
                if image == nil {
                    image = UIImage(named: "noImage.jpg")
                }
                DispatchQueue.main.async { cell.ivArticleImage?.image = image }
            } else {
                print(error!.localizedDescription)
            }
        }
        
        /* 取得User大頭圖 **/
        requestParam["action"] = "getImage"
        requestParam["id"] = article.userId
        //imageSize > 圖片尺寸
        requestParam["imageSize"] = cell.frame.width
        var userIcon: UIImage?
        executeTask(url_userAccount!, requestParam) { data, response, error in
            if error == nil {
                if data != nil {
                    userIcon = UIImage(data: data!)
                }
                if userIcon == nil {
                    userIcon = UIImage(named: "noImage.jpg")
                }
                DispatchQueue.main.async { cell.userIcon?.image = userIcon }
            } else {
                print(error!.localizedDescription)
            }
        }
        cell.articleTitle.text = article.articleTitle
        cell.userName.text = article.userName
        cell.lbArticleTime.text = article.modifyTime
        cell.resCategoryInfo.text = article.resCategoryInfo
        cell.resName.text = article.resName
        cell.lbgoodCount.text = String(article.articleGoodCount ?? 0)
        cell.lbCommentCount.text = String(article.commentCount ?? 0)
        cell.lbFavoriteArticle.text = String(article.favoriteCount ?? 0)
        return cell
    }
    
    /* 包裝資料到Detail **/
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selectedArticle = allArticle[indexPath.row]
        if let viewController = storyboard?.instantiateViewController(identifier: "ArticleDetailViewController") as? ArticleDetailViewController {
            viewController.articleDetail = selectedArticle
            navigationController?.pushViewController(viewController, animated: true)
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
    }
    */

    // MARK: UICollectionViewDataSource


    // MARK: UICollectionViewDelegate

    /*
    // Uncomment this method to specify if the specified item should be highlighted during tracking
    override func collectionView(_ collectionView: UICollectionView, shouldHighlightItemAt indexPath: IndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment this method to specify if the specified item should be selected
    override func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment these methods to specify if an action menu should be displayed for the specified item, and react to actions performed on the item
    override func collectionView(_ collectionView: UICollectionView, shouldShowMenuForItemAt indexPath: IndexPath) -> Bool {
        return false
    }

    override func collectionView(_ collectionView: UICollectionView, canPerformAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) -> Bool {
        return false
    }

    override func collectionView(_ collectionView: UICollectionView, performAction action: Selector, forItemAt indexPath: IndexPath, withSender sender: Any?) {
    
    }
    */

}
