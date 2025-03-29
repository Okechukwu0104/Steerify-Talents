
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <footer className="border-t border-neutral-200 bg-white">
      <div className="container-padding py-12 md:py-16">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-8">
          <div>
            <div className="flex items-center gap-2 mb-6">
              <div className="w-8 h-8 bg-primary rounded-full"></div>
              <span className="text-xl font-semibold">Steerify</span>
            </div>
            <p className="text-neutral-600 text-sm">
              Talent Management Platform
            </p>
          </div>
          
          <div>
            <h4 className="font-semibold mb-4">Platform</h4>
            <ul className="space-y-3 text-sm text-neutral-600">
              <li><a href="#features" className="hover:text-primary transition-colors">Features</a></li>
              <li><Link to="/pricing" className="hover:text-primary transition-colors">Pricing</Link></li>
              <li><Link to="/testimonials" className="hover:text-primary transition-colors">Testimonials</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Company</h4>
            <ul className="space-y-3 text-sm text-neutral-600">
              <li><Link to="/about" className="hover:text-primary transition-colors">About</Link></li>
              <li><Link to="/blog" className="hover:text-primary transition-colors">Blog</Link></li>
              <li><Link to="/careers" className="hover:text-primary transition-colors">Careers</Link></li>
            </ul>
          </div>

          <div>
            <h4 className="font-semibold mb-4">Account</h4>
            <ul className="space-y-3 text-sm text-neutral-600">
              <li><Link to="/login" className="hover:text-primary transition-colors">Log in</Link></li>
              <li><Link to="/signup" className="hover:text-primary transition-colors">Sign up</Link></li>
              <li><Link to="/help" className="hover:text-primary transition-colors">Help Center</Link></li>
            </ul>
          </div>
        </div>

        <div className="border-t border-neutral-200 mt-12 pt-8 flex flex-col md:flex-row justify-between items-center gap-4">
          <p className="text-sm text-neutral-600">
            © 2024 Steerify Talents. All rights reserved.
          </p>
          <div className="flex gap-6">
            <a href="#" className="text-neutral-600 hover:text-primary transition-colors">
              Twitter
            </a>
            <a href="#" className="text-neutral-600 hover:text-primary transition-colors">
              LinkedIn
            </a>
            <a href="#" className="text-neutral-600 hover:text-primary transition-colors">
              Instagram
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
