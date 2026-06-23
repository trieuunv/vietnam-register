import React from 'react';

import styles from './Banner.module.scss';

import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/css';

interface Slide {
    url: string,
    target: string,
}

interface BannerProps {
    slides: Slide[]
}

const Banner: React.FC<BannerProps> = ({ slides }) => {
    return (
        <div className={styles.container}>
            <Swiper
                spaceBetween={50}
                slidesPerView={1}
                onSlideChange={() => console.log('slide change')}
                onSwiper={(swiper) => console.log(swiper)}
                style={{ width: '100%' }}
            >
                { slides.map(slide => (
                    <SwiperSlide>
                        <div className={styles.slideWrapper}>
                            <img src={slide.url} alt="" className={styles.slideImage} />
                        </div>
                    </SwiperSlide>
                )) }
            </Swiper>
        </div>
    );
};

export default Banner;