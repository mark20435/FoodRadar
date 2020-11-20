//
//  MyArticleVController.swift
//  FoodRadar_iOS
//
//  Created by MyMBP on 2020/11/16.
//

import UIKit

class MyArticleVController: UIViewController {
    @IBOutlet weak var myArticleSegment: UISegmentedControl!
    
    @IBOutlet weak var myArticleTableView: UITableView!
    
    let urlMyArticleServlet = "MyArticleServlet"
    var myArticleArray: [MyArticleGetAllById]?
    var myArticleMyCommentArray: [MyArticleMyComment]?
//    var myArticleArrayAll: [MyArticleGetAllById]?
    var segmentIndex: Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        myArticleTableView.dataSource = self
        myArticleTableView.delegate = self

        segmentIndex = 0
        
        // Do any additional setup after loading the view.
//        print("USER_ID(1): \(COMM_USER_ID)")
        
        getMyArticleGetAllById(userId: COMM_USER_ID) { (myArticleArray) in
            if let myArticleArray = myArticleArray {
                self.myArticleArray = myArticleArray
                dump(self.myArticleArray)
//                self.myArticleArrayAll = self.myArticleArray
                DispatchQueue.main.async {
                    self.myArticleTableView.reloadData()
                }
            }
        }
        
        // Table隔線只顯示到有資料的部分
        myArticleTableView.tableFooterView = UIView()
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    @IBAction func clickMyArticleSegment(_ sender: UISegmentedControl) {
        
        segmentIndex = sender.selectedSegmentIndex
        print ("sender.segmentIndex",segmentIndex)
        switch segmentIndex {
        case 0:
            getMyArticleGetAllById(userId: COMM_USER_ID) { (myArticleArray) in
                if let myArticleArray = myArticleArray {
                    self.myArticleArray = myArticleArray
                    dump(self.myArticleArray)
    //                self.myArticleArrayAll = self.myArticleArray
                    DispatchQueue.main.async {
                        self.myArticleTableView.reloadData()
                    }
                }
            }
        case 1:
            getMyArticleIsMe(userId: COMM_USER_ID) { (myArticleArray) in
                if let myArticleArray = myArticleArray {
                    self.myArticleArray = myArticleArray
                    dump(self.myArticleArray)
                    DispatchQueue.main.async {
                        self.myArticleTableView.reloadData()
                    }
                }
            }
        case 2:
            getMyArticleMyComment(userId: COMM_USER_ID) { (myArticleMyCommentArray) in
                if let myArticleMyCommentArray = myArticleMyCommentArray {
                    self.myArticleMyCommentArray = myArticleMyCommentArray
                    dump(self.myArticleArray)
                    DispatchQueue.main.async {
                        self.myArticleTableView.reloadData()
                    }
                }
            }
        default:
            break
        }
    }
}

extension MyArticleVController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        var returnCnt = 0
        if segmentIndex == 0 || segmentIndex == 1 {
            returnCnt = myArticleArray?.count ?? 0
        } else if segmentIndex == 2 {
            returnCnt = myArticleMyCommentArray?.count ?? 0
        }
        return returnCnt
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "\(MyArticleVCell.self)", for: indexPath) as!
            MyArticleVCell
        
//        print("segmentIndex",segmentIndex)
        if segmentIndex == 0 || segmentIndex == 1 {
        
            let myArticleGetAllById = myArticleArray?[indexPath.row]
            
            cell.labelArticleTitle.text = myArticleGetAllById?.articleTitle
            
            let dataFormatter = DateFormatter()
            dataFormatter.locale = Locale(identifier: "zh_Hant_TW")
            dataFormatter.dateFormat = "YYYY-MM-dd"
            let articleTime = dataFormatter.string(from: (myArticleGetAllById?.articleTime)!)
            cell.labelArticleTime.text = "發文日期: " + articleTime
            
            cell.labelUserName.text = "發文者: " + myArticleGetAllById!.userName
            cell.labelArticleText.text = "內文: " + myArticleGetAllById!.articleText
            
            cell.imageView?.contentMode = .scaleAspectFill
            let url = NetworkController().baseURL.appendingPathComponent(urlMyArticleServlet)
            let imageSize = 400
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            
            let getImagePost = MyResGetImageStruct(id: myArticleGetAllById?.articleId ?? 0, imageSize: imageSize)
            request.httpBody = try? JSONEncoder().encode(getImagePost)
            URLSession.shared.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    DispatchQueue.main.async {
    //                    cell.ivMyRes?.image = nil
                        cell.imageArticle?.image = UIImage(data: data)
    //                    self.tableView.reloadData()
                    }
                }
    //            else {
    //                cell.imageView?.image = UIImage(named: "logo_foodradar")
    //            }
            }.resume()
        
        } else if segmentIndex == 2 {
            let myArticleMyComment = myArticleMyCommentArray?[indexPath.row]
            
            cell.labelArticleTitle.text = myArticleMyComment?.articleTitle
            
            let dataFormatter = DateFormatter()
            dataFormatter.locale = Locale(identifier: "zh_Hant_TW")
            dataFormatter.dateFormat = "YYYY-MM-dd"
            let articleTime = dataFormatter.string(from: (myArticleMyComment?.commentTime)!)
            cell.labelArticleTime.text = "回文日期: " + articleTime
            
            cell.labelUserName.text = "發文者: " + myArticleMyComment!.userName
            cell.labelArticleText.text = "回文: " + myArticleMyComment!.commentText
            
            cell.imageView?.contentMode = .scaleAspectFill
            let url = NetworkController().baseURL.appendingPathComponent(urlMyArticleServlet)
            let imageSize = 400
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            
            let getImagePost = MyResGetImageStruct(id: myArticleMyComment?.articleId ?? 0, imageSize: imageSize)
            request.httpBody = try? JSONEncoder().encode(getImagePost)
            URLSession.shared.dataTask(with: request) { (data, response, error) in
                if let data = data {
                    DispatchQueue.main.async {
                        cell.imageArticle?.image = UIImage(data: data)
                    }
                }
                
            }.resume()
        }
        
        return cell
    }
   
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath)
    {
        let verticalPadding: CGFloat = 8
        let horizontalPadding: CGFloat = 8

        let maskLayer = CALayer()
        maskLayer.cornerRadius = 10    //if you want round edges
        maskLayer.backgroundColor = UIColor.black.cgColor
        maskLayer.frame = CGRect(x: cell.bounds.origin.x, y: cell.bounds.origin.y, width: cell.bounds.width, height: cell.bounds.height).insetBy(dx: horizontalPadding, dy: verticalPadding/2)
        cell.layer.mask = maskLayer
    }

}

extension MyArticleVController {
    
    func getMyArticleGetAllById (userId: Int, complection: @escaping ([MyArticleGetAllById]?) -> Void ) {
        
        print("getMyArticle.userId: \(userId)")
        
        let url = NetworkController().baseURL.appendingPathComponent(urlMyArticleServlet)

        var request = URLRequest(url: url)
        let myArticleGetAllByIdStruct = MyArticleGetAllByIdStruct(id: userId)
        
        request.httpMethod = "POST"
        request.httpBody = try? JSONEncoder().encode(myArticleGetAllByIdStruct)
//        print("request: \(String(data: myArticleGetAllByIdStruct, encoding: .utf8))")
//        print("data:\(String(data: request, encoding: .utf8))")
        
        URLSession.shared.dataTask(with: request) {(dataTask, responseTask, errorTask) in
            let decoder = JSONDecoder()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            dateFormatter.calendar = Calendar(identifier: .iso8601)
            decoder.dateDecodingStrategy = .formatted(dateFormatter)
            if let dataFromDb = dataTask,
               let myArticleArray = try? decoder.decode([MyArticleGetAllById].self, from: dataFromDb) {
//                        print("data:\(String(data: dataFromDb, encoding: .utf8))")

//                print("resId: \(myResArray[0].resId)")
                complection (myArticleArray)
             
            } else {
                complection (nil)
            }
        }.resume()
    }
    
    func getMyArticleIsMe (userId: Int, complection: @escaping ([MyArticleGetAllById]?) -> Void ) {
        
        print("getMyArticleIsMe.userId: \(userId)")
        
        let url = NetworkController().baseURL.appendingPathComponent(urlMyArticleServlet)

        var request = URLRequest(url: url)
        let myArticleGetAllByIdStruct = MyArticleIsMeStruct(id: userId)
        
        request.httpMethod = "POST"
        request.httpBody = try? JSONEncoder().encode(myArticleGetAllByIdStruct)
        
        URLSession.shared.dataTask(with: request) {(dataTask, responseTask, errorTask) in
            let decoder = JSONDecoder()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            dateFormatter.calendar = Calendar(identifier: .iso8601)
            decoder.dateDecodingStrategy = .formatted(dateFormatter)
            if let dataFromDb = dataTask,
               let myArticleArray = try? decoder.decode([MyArticleGetAllById].self, from: dataFromDb) {
//                        print("data:\(String(data: dataFromDb, encoding: .utf8))")

//                print("resId: \(myResArray[0].resId)")
                complection (myArticleArray)
             
            } else {
                complection (nil)
            }
        }.resume()
    }
    
    
    func getMyArticleMyComment (userId: Int, complection: @escaping ([MyArticleMyComment]?) -> Void ) {
        
        print("getMyArticleMyComment.userId: \(userId)")
        
        let url = NetworkController().baseURL.appendingPathComponent(urlMyArticleServlet)

        var request = URLRequest(url: url)
        let myArticleGetAllByIdStruct = MyArticleMyCommentStruct(id: userId)
        
        request.httpMethod = "POST"
        request.httpBody = try? JSONEncoder().encode(myArticleGetAllByIdStruct)
        
        URLSession.shared.dataTask(with: request) {(dataTask, responseTask, errorTask) in
            let decoder = JSONDecoder()
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            dateFormatter.calendar = Calendar(identifier: .iso8601)
            decoder.dateDecodingStrategy = .formatted(dateFormatter)
            if let dataFromDb = dataTask,
               let myArticleMyCommentArray = try? decoder.decode([MyArticleMyComment].self, from: dataFromDb) {
                        print("data:\(String(data: dataFromDb, encoding: .utf8))")

//                print("resId: \(myResArray[0].resId)")
                complection (myArticleMyCommentArray)
             
            } else {
                complection (nil)
            }
        }.resume()
    }
    
}
