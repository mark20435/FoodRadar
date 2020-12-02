//
//  CommentViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/9.
//

import UIKit

class CommentViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    @IBOutlet weak var tfComment: UITextField!
    @IBOutlet weak var commentTableView: UITableView!
    var commentArray = [Comment]()
    var commentFavorite: Bool?
    var articleId: Int?
    var userId: Int?    //要顯示user資訊用
    let url_comment = URL(string: common_url + "CommentServlet")
    let url_commentGood = URL(string: common_url + "CommentGoodServlet")
    let url_userAccount = URL(string: common_url + "UserAccountServlet")
    let url_server = URL(string: common_url + "ArticleServlet")
    var loginUserId  = COMM_USER_ID
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getComment()
        addKeyboardObserver()   //keyboard設定
        tableViewAddRefreshControl() //呼叫下拉更新
    }
    /* 下拉更新 **/
    func tableViewAddRefreshControl() {
        let refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "下拉更新")
        refreshControl.addTarget(self, action: #selector(getComment), for: .valueChanged)
        commentTableView.refreshControl = refreshControl
    }
    override func viewWillAppear(_ animated: Bool) {
        getComment()
    }
    /* tableView長度 **/
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return commentArray.count
    }
    /* tableView內容 **/
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CommentTableViewCell") as! CommentTableViewCell
        var comment = commentArray[indexPath.row]
        cell.comment = comment  //cell接收 cell的資料
        cell.userName.text = comment.userName
        cell.commentText.text = comment.commentText
        //顯示留言更新時間
        if comment.commentModifyTime != comment.commentTime {
            cell.commentTime.text = "已編輯：\(comment.commentModifyTime!)"
        } else {
            cell.commentTime.text = "留言時間：\(comment.commentTime!)"
        }
        //顯示點讚狀態       //遊客不給點讚
        if COMM_USER_ID <= 0 {
//            comment.commentGoosStatus = false
            cell.commentGoodIcon.isSelected = false
            cell.commentGoodIcon.isEnabled = false
        } else {
            if comment.commentGoodStatus == true {
                cell.commentGoodIcon.isSelected = true
                comment.commentGoodStatus = true
            } else {
                cell.commentGoodIcon.isSelected = false
                comment.commentGoodStatus = true
            }
        }
        
        //顯示點讚數
        cell.lbCommentGoodCount.text = String(comment.commentGoodCount ?? 0)
        /* 取得使用者大頭 **/
                var requestParam = [String: Any]()
                requestParam["action"] = "getImage"
                requestParam["id"] = comment.userId
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
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let good = commentArray[indexPath.row]
        
        print("5566::\(indexPath.row)")
        let cell = tableView.dequeueReusableCell(withIdentifier: "CommentTableViewCell") as! CommentTableViewCell
        cell.commentGoodIcon.isSelected = good.commentGoodStatus ?? false
        
    }
    
    /* 取得留言方法 **/
    @objc func getComment() {
        var requestParam = [String : Any]()
        requestParam["action"] = "findCommentById"
        requestParam["articleId"] = articleId
        requestParam["loginUserId"] = COMM_USER_ID
        _ = executeTask(self.url_comment!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    if let result = try? JSONDecoder().decode([Comment].self, from: data!) {
                        self.commentArray = result
                        DispatchQueue.main.async {
                            print("測試留言點讚：\(dump(self.commentArray))")
                            //宣告物件執行refreshControl
                            if let control = self.commentTableView.refreshControl {
                                if control.isRefreshing {
                                    //假如已執行拉更新動作時，停止下拉
                                    control.endRefreshing()
                                }
                            }
                            self.commentTableView.reloadData()
                        }
                    }
                }
            }
        }
    }
    
    /* 左滑更新及刪除 **/
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
        var comment: String?
        /* 編輯留言動作 **/
        let update = UIContextualAction(style: .normal, title: "編輯") { (action, view, bool) in
            let controller = UIAlertController(title: "編輯留言", message: " ", preferredStyle: .alert)
            controller.addTextField { (textField) in
                let comment = self.commentArray[indexPath.row]
                textField.text = comment.commentText
            }
            let okAction = UIAlertAction(title: "送出", style: .destructive) { (_) in
                comment = controller.textFields?[0].text
                //假如沒輸入留言，就直接return(沒修改，更新狀態也不會變)
            if comment?.count != 0 {
                self.updateComment(indexPath, comment ?? "沒有資料")
            } else {
                return
            }
               
            }
            controller.addAction(okAction)
            
            let cancelAction = UIAlertAction(title: "取消", style: .cancel, handler: nil)
            controller.addAction(cancelAction)
            self.present(controller, animated: true, completion: nil)
            self.getComment()
        }
        
        update.backgroundColor = #colorLiteral(red: 0.925793469, green: 0.5111739635, blue: 0.4860615134, alpha: 1) // 改變背景色
        
        /* 刪除留言功能 **/
        let delete = UIContextualAction(style: .destructive, title: "刪除") { (action, view, bool) in
            //寫刪除功能
            let commentId = self.commentArray[indexPath.row].commentId
            let comment = DeleteComment(commentStatus: false, commentId: commentId)
            var requestParam = [String: Any]()
            requestParam["action"] = "commentDelete"
            requestParam["comment"] = try! String(data: JSONEncoder().encode(comment), encoding: .utf8)
            _  = executeTask(self.url_comment!, requestParam) { (data, response, error) in
                if error == nil {
                    if data != nil {
                        // 將輸入資料列印出來除錯用
                        print("input: \(String(data: data!, encoding: .utf8)!)")
                    }
                }
            }
            self.commentArray.remove(at: indexPath.row) //刪除畫面選擇列
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
        delete.backgroundColor = .red
        //左滑畫面位置配置 > 依序右到左
        let swipeAction = UISwipeActionsConfiguration(actions: [delete, update])
        //是否開啟滑倒底執行的動作 > 執行第一個動作(delete)
        swipeAction.performsFirstActionWithFullSwipe = true
        if loginUserId == commentArray[indexPath.row].userId {
            return swipeAction
        } else {
            return nil
        }
    }
    
    /* 編輯留言 **/
    func updateComment(_ indexPath: IndexPath, _ comment: String) {
//        let comment = commentTableView.indexPathForSelectedRow
        let updateComment = commentArray[indexPath.row]
        let commentId = updateComment.commentId
        let commentText = comment
        //設定時間格式，並轉為字串
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd HH:mm:ss"
        let timeString = dateFormatter.string(from: Date())
        let commentUpdate = UpdateComment(commentId: commentId, commentText: commentText, commentModifyTime: timeString)
        var requestParam = [String: Any]()
     requestParam["action"] = "commentUpdate"
        requestParam["comment"] = try! String(data: JSONEncoder().encode(commentUpdate), encoding: .utf8)
        _ = executeTask(self.url_comment!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    if let result = String(data: data!, encoding: .utf8) {
                        if Int(result) != nil {
                            DispatchQueue.main.async {
                                self.getComment()
                            }
                        }
                    }
                }
            }
        }
    }
    
    /* 送出留言 **/
    @IBAction func sendComment(_ sender: UIButton) {
        let commentText = tfComment == nil ? "" : tfComment.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        if loginUserId > 0 {
            if commentText?.count != 0 {
                //輸入留言資料
                //存成類別 > userId等Blues弄完要補
                let insertComment = InsertComment(commentId: 0, articleId: articleId, userId: loginUserId, commentStatus: true, commentText: commentText)
                var requestParam = [String: Any]()
                requestParam["action"] = "commentInsert"
                requestParam["comment"] = try? String(data: JSONEncoder().encode(insertComment), encoding: .utf8)
                    _ = executeTask(self.url_comment!, requestParam) { (data, response, error) in
                    if error == nil {
                        if data != nil {
                            if let result = String(data: data!, encoding: .utf8) {
                                if Int(result) != nil {
                                    DispatchQueue.main.async {
                                        self.getComment()
                                        self.TextButtonClicked(self.tfComment)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                let controller = UIAlertController(title: "輸入留言", message: "說一點話吧～", preferredStyle: .alert)
                //取消
                let noAction = UIAlertAction(title: "好的", style: .default, handler: nil)
                controller.addAction(noAction)
                present(controller, animated: true, completion: nil)
            }
        } else if (loginUserId <= 0) {
            let controller = UIAlertController(title: "請先登入", message: "你還沒登入喔！", preferredStyle: .alert)
            //取消
            let noAction = UIAlertAction(title: "好的", style: .default, handler: nil)
            controller.addAction(noAction)
            present(controller, animated: true, completion: nil)
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
/* 擴展方法 > keyboard **/
extension CommentViewController {
    //鍵盤輸入資料
    func hideKeyboard() {
        tfComment.resignFirstResponder()
    }
    /* 鍵盤狀態顯示監聽 **/
    func addKeyboardObserver() {
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(notification:)), name: UIResponder.keyboardDidShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(notification:)), name: UIResponder.keyboardDidHideNotification, object: nil)
    }
    
    /* 滑動時關閉鍵盤 **/
     func scrollViewDidScroll(_ scrollView: UIScrollView) {
        self.view?.endEditing(true)
    }
    /* 點擊Gone關掉鍵盤 **/
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.view?.endEditing(true)
    }
    /* 點擊旁邊關掉鍵盤 **/
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view?.endEditing(true)
    }
    
    /* 鍵盤顯示設定 **/
    @objc func keyboardWillShow(notification: Notification) {
        if let keyboardFrame = notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue {
            //彈出時設定
            let keyboardRect = keyboardFrame.cgRectValue    //keyboard形狀等於view的型狀(Rect > 矩形)
            let keyboardHight = keyboardRect.height     //keyboard高度為矩形高度
            view.frame.origin.y = -keyboardHight / 1.3 //螢幕高度為keyboard高度的1.3(負的是因為y軸原點為左上)
        } else {
            //未彈出時設定
            view.frame.origin.y = -view.frame.height / 3   //??
        }
    }
    /* keyboard隱藏設定 **/
    @objc func keyboardWillHide(notification: Notification) {
        view.frame.origin.y = 0     //鍵盤未彈出時畫面佔手機螢幕的高度位置為0
    }
    
    /* 複寫viewDidDisappear > ?? **/
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(true)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillHideNotification, object: nil)
    }
    
    /* 送出資料時收回鍵盤 **/
    func TextButtonClicked(_ textField: UITextField) {
        tfComment.text = ""
        tfComment.resignFirstResponder()
    }
    
}
