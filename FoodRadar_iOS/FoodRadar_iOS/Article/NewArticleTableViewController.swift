//
//  NewArticleTableViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/4.
//

import UIKit

class NewArticleTableViewController: UITableViewController {
  
    var allArticle: [Article]?
    let url_server = URL(string: common_url + "ArticleServlet")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        /* 連接到xib的Cell **/
        self.tableView.register(UINib(nibName: "ArticleTableViewCell", bundle: nil), forCellReuseIdentifier: "ArticleTableViewCell")

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem

    }
    /* 顯示抓取的資料 **/
    override func viewWillAppear(_ animated: Bool) {
        showAllArticles()
    }
    
    /* 抓取資料的方法 **/
    @objc func showAllArticles() {
        var requestParam = [String : Any]()
        requestParam["action"] = "getAllById"
        requestParam["loginUserId"] = 0
        executeTask(self.url_server!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    
                    if let result = try? JSONDecoder().decode([Article].self, from: data!) {
                        self.allArticle? = result
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
        return allArticle?.count ?? 1
    }
    
    /* 顯示xib到tableView上 **/
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let article = allArticle?[indexPath.row]
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ArticleTableViewCell") as! ArticleTableViewCell
        
        cell.articleTitle.text = article?.articleTitle
        cell.userName.text = article?.userName
        cell.lbArticleTime.text = article?.modifyTime
        cell.resCategoryInfo.text = article?.resCategoryInfo
        cell.resName.text = article?.resName
        cell.lbgoodCount.text = String(article?.articleGoodCount ?? 0)
        cell.lbCommentCount.text = String(article?.commentCount ?? 0)
        cell.lbFavoriteArticle.text = String(article?.favoriteCount ?? 0)
        return cell
    }

    // MARK: - Table view data source

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
         Get the new view controller using segue.destination.
         Pass the selected object to the new view controller.
    }
   }*/

}
