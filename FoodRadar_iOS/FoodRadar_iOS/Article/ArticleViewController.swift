//
//  ArticleViewController.swift
//  FoodRadar_iOS
//
//  Created by 陳暘璿 on 2020/11/4.
//

import UIKit

class ArticleViewController: UIViewController {
    
    @IBOutlet weak var toDetail: UIBarButtonItem!

    /* 控制頁面UISegmented **/
    @IBOutlet weak var ArticleSegmentedControl: UISegmentedControl!
    /* 連接UIScrollView **/
    @IBOutlet weak var scrollView: UIScrollView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //遊客不能點擊發文
        if COMM_USER_ID > 0 {
            self.toDetail.isEnabled = true
        } else {
            self.toDetail.isEnabled = false
        }
    }
    override func viewWillAppear(_ animated: Bool) {
        //遊客不能點擊發文
        if COMM_USER_ID > 0 {
            self.toDetail.isEnabled = true
        } else {
            self.toDetail.isEnabled = false
        }
    }
    
    
    /* 點選Segment 換頁動作 > Segmented **/
    @IBAction func changePage(_ sender: UISegmentedControl) {
        let x = CGFloat(sender.selectedSegmentIndex) * scrollView.bounds.width
        let offect = CGPoint(x: x, y: 0)
        scrollView.setContentOffset(offect, animated: true)
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

/* 擴展：連接segmented與scrollView > 滑動換頁 **/
extension ArticleViewController: UIScrollViewDelegate {
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let index = Int(scrollView.contentOffset.x / scrollView.bounds.width )
        ArticleSegmentedControl.selectedSegmentIndex = index
    }
}
