//
//  ResMapViewController.swift
//  FoodRadar_iOS
//
//  Created by Hsinwei Kao on 2020/11/10.
//

import UIKit
import MapKit
import CoreLocation

class ResMapViewController: UIViewController {

    @IBOutlet weak var mapView: MKMapView!
    let manager = CLLocationManager()
    var getLocation = true
    
    override func viewDidLoad() {
        super.viewDidLoad()

        manager.requestWhenInUseAuthorization()
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

extension ResMapViewController: MKMapViewDelegate {
    func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
        if getLocation {
            getLocation = false
            let region = MKCoordinateRegion(center: userLocation.coordinate, latitudinalMeters: 500, longitudinalMeters: 500)
            
            mapView.setRegion(region, animated: true)
        }
    }
}
