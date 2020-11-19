//
//  FavoriteArticleTableViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/4.
//

import UIKit

class FavoriteArticleTableViewController: UITableViewController, UISearchBarDelegate {
    
      var allArticle = [Article]()
      var searchArticle = [Article]()
      let url_server = URL(string: common_url + "ArticleServlet")
      let url_userAccount = URL(string: common_url + "UserAccountServlet")
      let url_image = URL(string: common_url + "ImgServlet")
    @IBOutlet weak var favoriteArticleSearchBar: UISearchBar!
    @IBOutlet var favoriteTableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        /* 連接到xib的Cell **/
        self.tableView.register(UINib(nibName: "ArticleTableViewCell", bundle: nil), forCellReuseIdentifier: "ArticleTableViewCell")
        /* 程式設定tableView Header 的高度，避免searchBar擋住tableView **/
        //註，searchBar要用view包住，不能直接放，避免畫面跑版
        self.favoriteTableView.tableHeaderView?.frame.size = CGSize(width: self.tableView.bounds.width, height: 56)
        favoriteTableView.delegate = self
        favoriteArticleSearchBar.delegate = self
        
        tableViewAddRefreshControl()    //呼叫下拉更新
    }
    
    /* 下拉更新 **/
    func tableViewAddRefreshControl() {
        let refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "下拉更新")
        refreshControl.addTarget(self, action: #selector(showAllArticles), for: .valueChanged)
        self.tableView.refreshControl = refreshControl
    }
    
    /* 搜尋設定 **/
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        let searchText = searchBar.text ?? ""
        if searchText == "" {
            searchArticle = allArticle
        } else {
            searchArticle = allArticle.filter({ (Article) -> Bool in
                return Article.articleTitle!.uppercased().contains(searchText.uppercased()) ||
                    Article.resName!.uppercased().contains(searchText.uppercased())
            })
        }
        favoriteTableView.reloadData()
    }
    /* 滑動時隱藏鍵盤 **/
    override func scrollViewDidScroll(_ scrollView: UIScrollView) {
        self.view?.endEditing(true)
    }
    /* 點擊虛擬鍵盤的按鈕時隱藏鍵盤 **/
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.view?.endEditing(true)
    }
    /* 點擊空白處隱藏鍵盤 **/
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view?.endEditing(true)
    }
    
    /* 顯示抓取的資料 **/
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if let selectedIndexPath = tableView.indexPathForSelectedRow {
            tableView.deselectRow(at: selectedIndexPath, animated: animated)
        }
        showAllArticles()
    }
    
    /* 抓取資料的方法 **/
    @objc func showAllArticles() {
        var requestParam = [String : Any]()
        requestParam["action"] = "getAllByIdFavorite"
        requestParam["loginUserId"] = 0 //先寫死(遊客)
        _ = executeTask(self.url_server!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    if let result = try? JSONDecoder().decode([Article].self, from: data!) {
                        self.allArticle = result
                        self.searchArticle = result
                        DispatchQueue.main.async {
                            if let control = self.tableView.refreshControl {
                                //假如已經正在執行下拉更新，停止下拉
                                if control.isRefreshing {
                                    control.endRefreshing()
                                }
                            }
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

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return searchArticle.count
    }
    
    /* 顯示xib到tableView上 **/
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ArticleTableViewCell") as! ArticleTableViewCell
        
        let article = searchArticle[indexPath.row]
        /* 取得餐廳圖片 **/
        var requestParam = [String: Any]()
        requestParam["action"] = "getImageByArticleId"
        requestParam["id"] = article.articleId
        //圖片寬度為tableViewCell的1/4，ImageView的寬度也建議在storyboard加上比例設定的constraint
        requestParam["imageSize"] = cell.frame.width
        //先預設圖檔為noImage.jpg > 防止重複利用
        cell.ivArticleImage.image = UIImage(named: "noImage.jpg")
        var image: UIImage?
        cell.task = executeTask(url_image!, requestParam) { data, response, error in
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
//        var requestParamUser = [String: Any]()
        requestParam["action"] = "getImage"
        requestParam["id"] = article.userId
        //imageSize > 圖片尺寸
        requestParam["imageSize"] = cell.frame.width
        var userIcon: UIImage?
        cell.task = executeTask(url_userAccount!, requestParam) { data, response, error in
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
        if article.modifyTime != article.articleTime {
            cell.lbArticleTime.text = "修改時間：\(article.modifyTime!)"
        } else {
            cell.lbArticleTime.text = "發文時間：\(article.articleTime!)"
        }
        cell.resCategoryInfo.text = article.resCategoryInfo
        cell.resName.text = article.resName
        cell.lbgoodCount.text = String(article.articleGoodCount ?? 0)
        cell.lbCommentCount.text = String(article.commentCount ?? 0)
        cell.lbFavoriteArticle.text = String(article.favoriteCount ?? 0)
    
        return cell
    }
    
    /* 包裝資料到Detail **/
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selectedArticle = searchArticle[indexPath.row]
        if let viewController = storyboard?.instantiateViewController(identifier: "ArticleDetailViewController") as? ArticleDetailViewController {
            viewController.articleDetail = selectedArticle
            navigationController?.pushViewController(viewController, animated: true)
        }
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
