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
    var articleId: Int?
    var userId: Int?
    let url_comment = URL(string: common_url + "CommentServlet")
    let url_userAccount = URL(string: common_url + "UserAccountServlet")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        getComment()
        addKeyboardObserver()   //keyboard設定
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return commentArray.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CommentTableViewCell") as! CommentTableViewCell
        let comment = commentArray[indexPath.row]
    
        cell.userName.text = comment.userName
        cell.commentText.text = comment.commentText
        cell.commentTime.text = comment.commentTime
        cell.commentGoodCount.text = String(comment.commentGoodCount ?? 0)
        
        /* 取得使用者大頭 **/
                var requestParam = [String: Any]()
                requestParam["action"] = "getImage"
                requestParam["id"] = comment.userId
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
        return cell
    }
    
    /* 取得留言方法 **/
    @objc func getComment() {
        var requestParam = [String : Any]()
        requestParam["action"] = "findCommentById"
        requestParam["articleId"] = articleId
        executeTask(self.url_comment!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    // 將輸入資料列印出來除錯用
                    print("input: \(String(data: data!, encoding: .utf8)!)")
                    
                    if let result = try? JSONDecoder().decode([Comment].self, from: data!) {
                        self.commentArray = result
                        DispatchQueue.main.async {
                            self.commentTableView.reloadData()
                        }
                    }
                }
            }
        }
        
    }
    
    /* 送出留言 **/
    @IBAction func sendComment(_ sender: UIButton) {
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
    
    /* 鍵盤顯示設定 **/
    @objc func keyboardWillShow(notification: Notification) {
        if let keyboardFrame = notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue {
            //彈出時設定
            let keyboardRect = keyboardFrame.cgRectValue    //設定keyboard形狀等於view的型狀(Rect > 矩形)
            let keyboardHight = keyboardRect.height     //keyboard高度為矩形高度
            view.frame.origin.y = -keyboardHight / 2    //螢幕高度為keyboard高度的一半(負的是因為y軸原點為左上)
        } else {
            //未彈出時設定??
            view.frame.origin.y = -view.frame.height / 3    //??
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
    
}
