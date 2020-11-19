//
//  CommentTableViewCell.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/9.
//

import UIKit

class CommentTableViewCell: UITableViewCell {
    @IBOutlet weak var userName: UILabel!
    @IBOutlet weak var userIcon: UIImageView!
    @IBOutlet weak var commentText: UILabel!
    @IBOutlet weak var commentGoodIcon: UIButton!
    @IBOutlet weak var lbCommentGoodCount: UILabel!
    @IBOutlet weak var commentTime: UILabel!
    var comment: Comment!
    
    let url_comment = URL(string: common_url + "CommentServlet")
    let url_commentGood = URL(string: common_url + "CommentGoodServlet")
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    /* 解決重覆利用 **/
    var task: URLSessionDataTask?
    override func prepareForReuse() {
        super.prepareForReuse() //重複利用之前呼叫
        task?.cancel()
    }
    
    /* 設定點讚，呼叫點讚方法 **/
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        self.commentGoodIcon.addTarget(self, action: #selector(setCommentGood), for: .touchDown)
        // Configure the view for the selected state
    }
    /* 點讚功能 **/
    @objc func setCommentGood(){
        print("commentGoodIcon.isSelected::\(commentGoodIcon.isSelected)"  )
        var requestParam = [String: Any]()
        if comment?.commentGoodStatus == true {
            commentGoodIcon.isSelected = true
            requestParam["action"] = "commentGoodDelete"
            requestParam["commentId"] = comment?.commentId
            requestParam["userId"] = COMM_USER_ID
            _ = executeTask(self.url_commentGood!, requestParam) { (data, response, error) in
                if error == nil {
                    if data != nil {
                        if let result = String(data: data!, encoding: .utf8) {
                            if Int(result) != nil {
                                DispatchQueue.main.async {
                                    //把commentGoodCount減1後，再丟回commentGoodCount
                                   self.comment.commentGoodCount = self.comment.commentGoodCount! - 1
                                    self.lbCommentGoodCount.text = String(self.comment.commentGoodCount!)
                                    self.commentGoodIcon.isSelected.toggle()
                                    self.comment?.commentGoodStatus = false
                                }
                            }
                        }
                    }
                } else {
                    print(error!.localizedDescription)
                }
            }
        } else {
            commentGoodIcon.isSelected = false
            let commentGood = SetCommentGood(userId: COMM_USER_ID, commentId: comment?.commentId)
            requestParam["action"] = "commentGoodInsert"
            requestParam["commentGood"] = try? String(data: JSONEncoder().encode(commentGood), encoding: .utf8)
            _ = executeTask(self.url_commentGood!, requestParam) { (data, response, error) in
                if error == nil {
                    if data != nil {
                        if let result = String(data: data!, encoding: .utf8) {
                            if Int(result) != nil {
                                DispatchQueue.main.async {
                                    //把commentGoodCount加1後，再丟回commentGoodCount
                                    self.comment.commentGoodCount = self.comment.commentGoodCount! + 1
                                    self.lbCommentGoodCount.text = String(self.comment.commentGoodCount!)
                                    self.commentGoodIcon.isSelected.toggle()
                                    self.comment?.commentGoodStatus = true
                                }
                            }
                        }
                    }
                } else {
                    print(error!.localizedDescription)
                }
            }
        }
    }
    
}

//self.articleShow.articleGoodCount =  self.articleShow.articleGoodCount! - 1
//self.articleGoodCount.text = String(self.articleShow.articleGoodCount!)
//self.btArticleGood.isSelected.toggle()
//self.articleDetail.articleGoodStatus = false
