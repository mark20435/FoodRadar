//
//  BigImageViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/7.
//

import UIKit

class BigImageViewController: UIViewController, UIScrollViewDelegate {

    @IBOutlet weak var bigImageView: UIImageView!
    var articleDetail: Article!
    var articleImage: Image!
    var imageId: Int!
    let url_image = URL(string: common_url + "ImgServlet")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = ""
        showBigImage()

    }
    
    /* 縮放ImageView的方法 > 需繼承UIScrollViewDelegate */
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        // return可加可不加
        return bigImageView
    }
    
    /* 顯示大圖 **/
    func showBigImage() {
        var requestParam = [String: Any]()
        requestParam["action"] = "getImage"
        requestParam["id"] = imageId
        requestParam["imageSize"] = bigImageView.frame.width
        var image: UIImage?
        executeTask(url_image!, requestParam) { (data, response, error) in
            if error == nil {
                if data != nil {
                    image = UIImage(data: data!)
                }
                if image == nil {
                    image = UIImage(named: "noImage.jpg")
                }
                DispatchQueue.main.async {
                    self.bigImageView.image = image
                }
            } else {
                print(error!.localizedDescription)
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
}
