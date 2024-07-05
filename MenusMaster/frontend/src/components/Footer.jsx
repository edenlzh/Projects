import React from 'react'
import { AiFillHeart } from 'react-icons/ai'

import { images } from '../constants'

const Footer = () => {
  return <section className='bg-dark-hard'>
    <footer className='container mx-auto grid grid-cols-10 px-5 py-10 gap-y-10 gap-x-5 md:pt-20 md:grid-cols-12 lg:grid-col-10 lg:gap-x-10'>
      <div className='col-span-5 md:col-span-4 lg:col-span-2'>
        <h3 className='text-dark-light font-bold md:text-lg'>Home</h3>
        <ul className='text-[#959EAD] text-sm mt-5 space-y-4 md:text-base'>
          <li>
            <a href='/'>Go to the Home Page</a>
          </li>
        </ul>
      </div>
      <div className='col-span-5 md:col-span-4 lg:col-span-2'>
        <h3 className='text-dark-light font-bold md:text-lg'>Services</h3>
        <ul className='text-[#959EAD] text-sm mt-5 space-y-4 md:text-base'>
          <li>
            <a href='/menumaster'>Use Menu Master Service</a>
          </li>
        </ul>
      </div>
      <div className='col-span-5 md:col-span-4 md:col-start-5 lg:col-start-auto lg:col-span-2'>
        <h3 className='text-dark-light font-bold md:text-lg'>Articles</h3>
        <ul className='text-[#959EAD] text-sm mt-5 space-y-4 md:text-base'>
          <li>
            <a href='/blog'>See the Articles</a>
          </li>
        </ul>
      </div>
      <div className='col-span-5 md:col-span-4 lg:col-span-2'>
        <h3 className='text-dark-light font-bold md:text-lg'>About</h3>
        <ul className='text-[#959EAD] text-sm mt-5 space-y-4 md:text-base'>
          <li>
            <a href='/about'>Get to Know More About Us</a>
          </li>
        </ul>
      </div>
      <div className='col-span-10 md:order-first md:col-span-4 lg:col-span-2'>
        <img src={images.Logo} alt='logo' className='w-24 md:w-32 lg:w-40 brightness-0 invert mx-auto' />
      </div>
      <div className='hidden md:flex flex-col items-center space-y-4 md:col-span-12 lg:col-span-10 lg:flex lg:items-center lg:justify-center lg:ml-40'>
        <div className='bg-primary text-white p-3 rounded-full'>
          <AiFillHeart className='w-7 h-auto' />
        </div>
        <p className='font-bold italic text-dark-light'>Copyright © 2024. Crafted with love.</p>
      </div>
    </footer>
  </section>
}

export default Footer
