import UIKit

typealias LayoutConstraintView = (UIView, CGFloat)
typealias LayoutConstraintGuide = (UILayoutGuide, CGFloat)
typealias LayoutConstraintYAxis = (NSLayoutYAxisAnchor, CGFloat)
typealias LayoutConstraintXAxis = (NSLayoutXAxisAnchor, CGFloat)
typealias LayoutConstraintDimension = (NSLayoutDimension, CGFloat)


infix operator *: AdditionPrecedence
infix operator -*: AdditionPrecedence
infix operator |*: AdditionPrecedence
infix operator -*|: AdditionPrecedence
infix operator |-: AdditionPrecedence
infix operator -|: AdditionPrecedence
infix operator -: AdditionPrecedence
infix operator |: AdditionPrecedence
infix operator ^: AdditionPrecedence
infix operator !^: AdditionPrecedence
prefix operator !


extension UIView {
    
    static prefix func !(view: UIView) {
        view.translatesAutoresizingMaskIntoConstraints = false
    }
    
    static func + (lhs: UIView, rhs: UIView) {
        lhs.addSubview(rhs)
    }
    
    static func -*| (lhs: UIView, rhs: UIView) {
        lhs * rhs
        lhs - rhs
        lhs | rhs
    }
    
    static func -* (lhs: UIView, rhs: UIView) {
        lhs.centerXAnchor.constraint(equalTo: rhs.centerXAnchor).isActive = true
    }
    
    static func |* (lhs: UIView, rhs: UIView) {
        lhs.centerYAnchor.constraint(equalTo: rhs.centerYAnchor).isActive = true
    }
    
    static func | (lhs: UIView, rhs: UIView) {
        lhs.heightAnchor.constraint(equalTo: rhs.heightAnchor, multiplier: 1).isActive = true
    }
    
    static func | (lhs: UIView, rhs: CGFloat) {
        lhs.heightAnchor.constraint(equalToConstant: rhs).isActive = true
    }
    
    static func | (lhs: UIView, rhs: LayoutConstraintDimension) {
        lhs.heightAnchor.constraint(equalTo: rhs.0, multiplier: 1.0, constant: rhs.1).isActive = true
    }
    
    static func - (lhs: UIView, rhs: UIView) {
        lhs.widthAnchor.constraint(equalTo: rhs.widthAnchor, multiplier: 1).isActive = true
    }
    
    static func - (lhs: UIView, rhs: CGFloat) {
        lhs.widthAnchor.constraint(equalToConstant: rhs).isActive = true
    }
    
    static func |- (lhs: UIView, rhs: UIView) {
        lhs.leadingAnchor.constraint(equalTo: rhs.leadingAnchor).isActive = true
    }
    
    static func |- (lhs: UIView, rhs: UILayoutGuide) {
        lhs.leadingAnchor.constraint(equalTo: rhs.leadingAnchor).isActive = true
    }
    
    static func |- (lhs: UIView, rhs: LayoutConstraintGuide) {
        lhs.leadingAnchor.constraint(equalTo: rhs.0.leadingAnchor, constant: rhs.1).isActive = true
    }
    
    static func |- (lhs: UIView, rhs: LayoutConstraintView) {
        lhs.leadingAnchor.constraint(equalTo: rhs.0.leadingAnchor, constant: rhs.1).isActive = true
    }
    
    static func |- (lhs: UIView, rhs: LayoutConstraintXAxis) {
        lhs.leadingAnchor.constraint(equalTo: rhs.0, constant: rhs.1).isActive = true
    }
    
    static func -| (lhs: UIView, rhs: UIView) {
        lhs.trailingAnchor.constraint(equalTo: rhs.trailingAnchor).isActive = true
    }
    
    static func -| (lhs: UIView, rhs: UILayoutGuide) {
        lhs.trailingAnchor.constraint(equalTo: rhs.trailingAnchor).isActive = true
    }
    
    static func -| (lhs: UIView, rhs: LayoutConstraintView) {
        lhs.trailingAnchor.constraint(equalTo: rhs.0.trailingAnchor, constant: rhs.1).isActive = true
    }
    
    static func -| (lhs: UIView, rhs: LayoutConstraintGuide) {
        lhs.trailingAnchor.constraint(equalTo: rhs.0.trailingAnchor, constant: rhs.1).isActive = true
    }
    
    static func -| (lhs: UIView, rhs: LayoutConstraintXAxis) {
        lhs.trailingAnchor.constraint(equalTo: rhs.0, constant: rhs.1).isActive = true
    }
    
    static func * (lhs: UIView, rhs: UIView) {
        lhs.centerXAnchor.constraint(equalTo: rhs.centerXAnchor).isActive = true
        lhs.centerYAnchor.constraint(equalTo: rhs.centerYAnchor).isActive = true
    }
    
    static func * (lhs: UIView, rhs: UILayoutGuide) {
        lhs.centerXAnchor.constraint(equalTo: rhs.centerXAnchor).isActive = true
        lhs.centerYAnchor.constraint(equalTo: rhs.centerYAnchor).isActive = true
    }
    
    static func ^ (lhs: UIView, rhs: UIView) {
        lhs.topAnchor.constraint(equalTo: rhs.topAnchor).isActive = true
    }
    
    static func ^ (lhs: UIView, rhs: NSLayoutYAxisAnchor) {
        lhs.topAnchor.constraint(equalTo: rhs).isActive = true
    }
    
    static func ^ (lhs: UIView, rhs: UILayoutGuide) {
        lhs.topAnchor.constraint(equalTo: rhs.topAnchor).isActive = true
    }
    
    static func ^ (lhs: UIView, rhs: LayoutConstraintGuide) {
        lhs.topAnchor.constraint(equalTo: rhs.0.topAnchor, constant:rhs.1).isActive = true
    }
    
    static func ^ (lhs: UIView, rhs: LayoutConstraintYAxis) {
        lhs.topAnchor.constraint(equalTo: rhs.0, constant: rhs.1).isActive = true
    }
    
    static func !^ (lhs: UIView, rhs: UIView) {
        lhs.bottomAnchor.constraint(equalTo: rhs.bottomAnchor).isActive = true
    }
    
    static func !^ (lhs: UIView, rhs: NSLayoutYAxisAnchor) {
        lhs.bottomAnchor.constraint(equalTo: rhs).isActive = true
    }
    
    static func !^ (lhs: UIView, rhs: UILayoutGuide) {
        lhs.bottomAnchor.constraint(equalTo: rhs.bottomAnchor).isActive = true
    }
    
    static func !^ (lhs: UIView, rhs: LayoutConstraintYAxis) {
        lhs.bottomAnchor.constraint(equalTo: rhs.0, constant: rhs.1).isActive = true
    }
    static func !^ (lhs: UIView, rhs: LayoutConstraintGuide) {
        lhs.bottomAnchor.constraint(equalTo: rhs.0.bottomAnchor, constant:rhs.1).isActive = true
    }
    
}

